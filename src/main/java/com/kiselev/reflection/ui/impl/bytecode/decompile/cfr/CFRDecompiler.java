package com.kiselev.reflection.ui.impl.bytecode.decompile.cfr;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.DefaultByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.cfr.configuration.CFRBuilderConfiguration;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassStringUtils;
import org.benf.cfr.reader.api.ClassFileSource;
import org.benf.cfr.reader.entities.ClassFile;
import org.benf.cfr.reader.state.ClassFileSourceImpl;
import org.benf.cfr.reader.state.DCCommonState;
import org.benf.cfr.reader.state.TypeUsageCollector;
import org.benf.cfr.reader.state.TypeUsageInformation;
import org.benf.cfr.reader.util.CannotLoadClassException;
import org.benf.cfr.reader.util.bytestream.BaseByteData;
import org.benf.cfr.reader.util.bytestream.ByteData;
import org.benf.cfr.reader.util.getopt.GetOptParser;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;
import org.benf.cfr.reader.util.output.Dumper;
import org.benf.cfr.reader.util.output.IllegalIdentifierDump;
import org.benf.cfr.reader.util.output.StdIODumper;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/25/2017.
 */
public final class CFRDecompiler implements Decompiler {

    private Collection<byte[]> innerClasses = new ArrayList<>();

    private Map<String, Object> configuration = getDefaultConfiguration();

    @Override
    public String decompile(byte[] byteCode) {
        String className = ClassNameUtils.getClassName(byteCode);

        GetOptParser getOptParser = new GetOptParser();
        Options options = getOptParser.parse(getDefaultOptions(className), OptionsImpl.getFactory());

        ClassFileSource classFileSource = new ClassFileSourceImpl(options);
        DCCommonState dcCommonState = new CFRDCCommonState(options, classFileSource, byteCode);
        String path = options.getOption(OptionsImpl.FILENAME);

        ClassFile classFile = dcCommonState.getClassFileMaybePath(path);
        TypeUsageCollector collectingDumper = new TypeUsageCollector(classFile);
        IllegalIdentifierDump illegalIdentifierDump = IllegalIdentifierDump.Factory.get(options);
        Dumper dumper = new CFRBuilderDumper(collectingDumper.getTypeUsageInformation(),options,illegalIdentifierDump);
        dcCommonState.configureWith(classFile);
        classFile.loadInnerClasses(dcCommonState);
        classFile.analyseTop(dcCommonState);

        classFile.collectTypeUsages(collectingDumper);

        classFile.dump(dumper);
        return dumper.toString();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.putAll(configuration.getConfiguration());
    }

    @Override
    public void appendAdditionalClasses(Collection<byte[]> classes) {
        this.innerClasses = classes;
    }

    private class CFRBuilderDumper extends StdIODumper {

        private StringBuilder builder = new StringBuilder();

        public CFRBuilderDumper(TypeUsageInformation typeUsageInformation, Options options,
                                IllegalIdentifierDump illegalIdentifierDump) {
            super(typeUsageInformation, options, illegalIdentifierDump);
        }

        @Override
        protected void write(String data) {
            if (data.equals("implements ") || data.equals("extends ")) {
                builder.deleteCharAt(ClassStringUtils.getNumberOfLineSeparator(builder) - 1);
                builder.delete(ClassStringUtils.getFirstNonSpaceNumber(builder), builder.length());
                builder.append(" ");
            } else if (data.equals("\n") && builder.charAt(ClassStringUtils.getFirstNonSpaceNumber(builder)) == ',') {
                builder.append(" ");
                return;
            } else if (data.equals("{") && builder.charAt(builder.length() - 1) != ' ') {
                builder.append(" ");
            }

            builder.append(data);
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }

    private class CFRDCCommonState extends DCCommonState {
        private ByteCodeCollector codeCollector = new DefaultByteCodeCollector();

        private Map<String, ClassFile> classFileMap = new HashMap<>();

        private static final String EMPTY_MESSAGE = "";

        private final String outerClassName;

        public CFRDCCommonState(Options options, ClassFileSource classFileSource, byte[] byteCode) {
            super(options, classFileSource);
            this.outerClassName = ClassNameUtils.getClassName(byteCode);

            loadClassFile(byteCode);
            for (byte[] innerClass : innerClasses) {
                loadClassFile(innerClass);
            }
        }

        @Override
        public ClassFile getClassFile(String path) throws CannotLoadClassException {
            return loadClassFileAtPath(path);
        }

        private ClassFile loadClassFileAtPath(String path) {
            if (path.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
                path = path.substring(0, path.lastIndexOf(Constants.Suffix.CLASS_FILE_SUFFIX));
            }

            ClassFile classFile = classFileMap.get(path);
            if (classFile != null) {
                return classFile;
            }

            if (path.contains(outerClassName + Constants.Symbols.DOLLAR)) {
                throw new CannotLoadClassException(EMPTY_MESSAGE, null);
            }

            String name = ClassNameUtils.normalizeFullName(path);
            return loadClassFile(getByteCode(name));
        }

        private Class<?> loadClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException exception) {
                throw new CannotLoadClassException(String.format("Can't load class: %s", className), exception);
            }
        }

        private byte[] getByteCode(String className) {
            Class<?> clazz = loadClass(className);
            byte[] byteCode = codeCollector.getByteCode(clazz);

            if (byteCode == null) {
                throw new CannotLoadClassException(String.format("Can't load class: %s", className), null);
            }

            return byteCode;
        }

        @Override
        public ClassFile getClassFileMaybePath(String pathOrName) throws CannotLoadClassException {
            return loadClassFileAtPath(pathOrName);
        }

        private ClassFile loadClassFile(byte[] bytecode) {
            String className = ClassNameUtils.getClassName(bytecode);
            ByteData data = new BaseByteData(bytecode);
            ClassFile classFile = new ClassFile(data, className, this);
            classFileMap.put(className, classFile);
            return classFile;
        }
    }

    private String[] getDefaultOptions(String className) {
        List<String> options = new ArrayList<>();

        options.add(className);

        for (Map.Entry<String, Object> entry : getDefaultConfiguration().entrySet()) {
            options.add("--" + entry.getKey());
            options.add(entry.getValue().toString());
        }

        return options.toArray(new String[options.size()]);
    }

    private Map<String, Object> getDefaultConfiguration() {
        return CFRBuilderConfiguration
                .getBuilderConfiguration()
                .replaceStringConcatToStringBuilder(false)
                .decompileSugarEnumInSwitch(true)
                .decompileSugarInEnums(true)
                .decompileSugarStringInEnums(true)
                .decompileSugarInArrayIteration(true)
                .decompileSugarInCollectionIteration(true)
                .decompileLambdaFunctions(true)
                .decompileInnerClasses(true)
                .hideUTF8Characters(true)
                .hideVeryLongStrings(false)
                .removeBoilerplateFunctions(true)
                .removeInnerClassesSynthetics(true)
                .hideBridgeMethods(true)
                .liftInitialisationToAllConstructors(true)
                .removeDeadMethods(false)
                .removeBadGenerics(false)
                .decompileSugarInAsserts(true)
                .decompileBoxing(true)
                .showCFRVersion(false)
                .decompileSugarInFinally(true)
                .removeSupportCodeForMonitors(false)
                .replaceMonitorWithComments(false)
                .lenientSituationsWhereThrowException(true)
                .getConfiguration();
    }
}
