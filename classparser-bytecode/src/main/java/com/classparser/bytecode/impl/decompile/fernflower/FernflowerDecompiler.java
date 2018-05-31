package com.classparser.bytecode.impl.decompile.fernflower;

import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;
import com.classparser.bytecode.impl.decompile.fernflower.configuration.FernflowerBuilderConfiguration;
import com.classparser.bytecode.impl.utils.ClassFileUtils;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.configuration.Configuration;
import com.classparser.exception.decompile.DecompilationException;
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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class FernflowerDecompiler implements Decompiler {

    private static final int CLASS_TYPE = 1;

    private static final int CLASS_FILE_TYPE = 0;

    private final Map<String, Object> configuration;

    private final IResultSaver saver;

    private final SourceCodeGetterInvocationHandler invocationHandler;

    private final BaseDecompiler decompiler;

    public FernflowerDecompiler() {
        ClassLoader classLoader = ClassFileUtils.getClassLoader(getClass());
        IFernflowerLogger logger = new PrintStreamLogger(System.out);
        Class<?>[] classes = new Class[]{IResultSaver.class};
        this.invocationHandler = new SourceCodeGetterInvocationHandler();
        this.saver = (IResultSaver) Proxy.newProxyInstance(classLoader, classes, invocationHandler);
        this.configuration = getDefaultConfiguration();
        this.decompiler = new BaseDecompiler(null, this.saver, configuration, logger);
    }

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        for (byte[] nestedClass : classes) {
            uploadBytecode(decompiler, nestedClass);
        }
        uploadBytecode(decompiler, byteCode);
        decompiler.decompileContext();

        return invocationHandler.getSource();
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
            String exceptionMessage =
                    MessageFormat.format("Can't upload bytecode of class: \"{0}\" to fernflower", className);
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
        return new ContextUnit(CLASS_FILE_TYPE, "", "", true, saver, fernflower);
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configurationManager) {
    }

    private static class SourceCodeGetterInvocationHandler implements InvocationHandler {

        private String source;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (method.getName().equals("saveClassFile")) {
                source = String.valueOf(args[3]);
            }
            return null;
        }

        public String getSource() {
            return source;
        }
    }
}