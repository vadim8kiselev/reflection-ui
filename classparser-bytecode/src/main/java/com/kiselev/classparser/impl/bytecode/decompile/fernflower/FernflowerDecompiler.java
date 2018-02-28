package com.kiselev.classparser.impl.bytecode.decompile.fernflower;

import com.kiselev.classparser.api.bytecode.decompile.Decompiler;
import com.kiselev.classparser.configuration.Configuration;
import com.kiselev.classparser.exception.decompile.DecompilationException;
import com.kiselev.classparser.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.classparser.impl.bytecode.decompile.fernflower.configuration.FernflowerBuilderConfiguration;
import com.kiselev.classparser.impl.bytecode.utils.ClassFileUtils;
import com.kiselev.classparser.impl.bytecode.utils.ClassNameUtils;
import org.jetbrains.java.decompiler.main.Fernflower;
import org.jetbrains.java.decompiler.main.decompiler.BaseDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.jetbrains.java.decompiler.struct.ContextUnit;
import org.jetbrains.java.decompiler.struct.StructClass;
import org.jetbrains.java.decompiler.struct.StructContext;
import org.jetbrains.java.decompiler.struct.lazy.LazyLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.jar.Manifest;

public final class FernflowerDecompiler implements Decompiler {

    private static final int CLASS_TYPE = 1;

    private static final int CLASS_FILE_TYPE = 0;

    private Map<String, Object> configuration = getDefaultConfiguration();

    private FernflowerResultSaver saver = new FernflowerResultSaver();

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        IFernflowerLogger logger = new PrintStreamLogger(System.out);
        BaseDecompiler decompiler = new BaseDecompiler(null, saver, configuration, logger);
        for (byte[] nestedClass : classes) {
            uploadBytecode(decompiler, nestedClass);
        }
        uploadBytecode(decompiler, byteCode);
        decompiler.decompileContext();

        return saver.getSource();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.putAll(configuration.getConfiguration());
    }

    private Map<String, Object> getDefaultConfiguration() {
        return FernflowerBuilderConfiguration
                .getBuilderConfiguration()
                .showBridgeMethods(true)
                .showMemberSyntheticClasses(true)
                .decompileInnerClasses(true)
                .collapseClassReferences(true)
                .decompileAssertions(true)
                .showEmptySuperInvocation(true)
                .showEmptyDefaultConstructor(true)
                .decompileGenericSignatures(true)
                .deInlineFinallyStructures(true)
                .assumeReturnNotThrowingExceptions(true)
                .decompileEnumerations(true)
                .removeGetClassInvocation(false)
                .showOutputNumericLiterals(false)
                .encodeNonASCIICharacters(true)
                .interpretInt1AsBooleanTrue(false)
                .allowForSetSyntheticAttribute(true)
                .considerNamelessTypes(false)
                .reconstructVariableNamesFromDebugInformation(true)
                .removeEmptyExceptionRanges(false)
                .setUpperLimitForDecompilation(0)
                .renameAmbiguousClassesAndClassElements(false)
                .checkNonNullAnnotation(true)
                .decompileLambdaExpressionsToAnonymousClasses(false)
                .setCountIndentSpaces(4)
                .setLogLevel(IFernflowerLogger.Severity.ERROR)
                .getConfiguration();
    }

    private void uploadBytecode(BaseDecompiler decompiler, byte[] byteCode) {
        try {
            Fernflower fernflower = getFernflower(decompiler);
            StructClass structClass = createClassStruct(byteCode);

            StructContext structContext = fernflower.getStructContext();
            Map<String, StructClass> classes = structContext.getClasses();
            classes.put(structClass.qualifiedName, structClass);

            Map<String, ContextUnit> units = getContextUnit(structContext);

            ContextUnit unit = createFakeContextUnit(fernflower);
            unit.addClass(structClass, structClass.qualifiedName + Constants.Suffix.CLASS_FILE_SUFFIX);
            units.put(structClass.qualifiedName, unit);
        } catch (Exception exception) {
            String className = ClassNameUtils.getClassName(byteCode);
            String exceptionMessage = String.format("Can't upload bytecode of class: %s to fernflower", className);
            throw new DecompilationException(exceptionMessage, exception);
        }
    }

    private Fernflower getFernflower(BaseDecompiler decompiler) throws NoSuchFieldException, IllegalAccessException {
        Field fieldFernflower = decompiler.getClass().getDeclaredField("fernflower");
        fieldFernflower.setAccessible(true);

        return Fernflower.class.cast(fieldFernflower.get(decompiler));
    }

    private StructClass createClassStruct(byte[] byteCode) throws IOException {
        LazyLoader lazyLoader = new LazyLoader((dummy, dummyTwo) -> byteCode);
        StructClass structClass = new StructClass(byteCode, true, lazyLoader);
        LazyLoader.Link link = new LazyLoader.Link(CLASS_TYPE, structClass.qualifiedName, "");
        lazyLoader.addClassLink(structClass.qualifiedName, link);

        return structClass;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ContextUnit> getContextUnit(StructContext context)
            throws NoSuchFieldException, IllegalAccessException {
        Field fieldUnits = StructContext.class.getDeclaredField("units");
        fieldUnits.setAccessible(true);

        return (Map<String, ContextUnit>) fieldUnits.get(context);
    }

    private ContextUnit createFakeContextUnit(Fernflower fernflower) {
        final String EMPTY_PATH = ClassFileUtils.EMPTY_PATH;
        return new ContextUnit(CLASS_FILE_TYPE, EMPTY_PATH, EMPTY_PATH, true, saver, fernflower);
    }

    private static class FernflowerResultSaver implements IResultSaver {

        private String source = "";

        @Override
        public void saveClassFile(String dummy, String dummyTwo, String dummyThree, String source, int[] dummyFour) {
            this.source = source;
        }

        private String getSource() {
            return source;
        }

        @Override
        public void createArchive(String dummy, String dummyTwo, Manifest manifest) {
        }

        @Override
        public void saveDirEntry(String dummy, String dummyTwo, String dummyThree) {
        }

        @Override
        public void copyEntry(String dummy, String dummyTwo, String dummyThree, String dummyFour) {
        }

        @Override
        public void saveClassEntry(String dummy, String dummyTwo, String dummyThree,
                                   String dummyFour, String dummyFive) {
        }

        @Override
        public void closeArchive(String dummy, String dummyTwo) {
        }

        @Override
        public void saveFolder(String dummy) {
        }

        @Override
        public void copyFile(String dummy, String dummyTwo, String dummyThree) {
        }
    }
}
