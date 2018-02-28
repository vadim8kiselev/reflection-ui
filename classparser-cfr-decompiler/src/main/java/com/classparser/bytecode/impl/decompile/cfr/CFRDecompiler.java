package com.classparser.bytecode.impl.decompile.cfr;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.collector.ChainByteCodeCollector;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.bytecode.impl.utils.RuntimeLibraryUploader;
import com.classparser.configuration.Configuration;
import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.decompile.cfr.configuration.CFRBuilderConfiguration;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This decompiler can't show local classes
 */
public final class CFRDecompiler implements Decompiler {

    private static final String DECOMPILER_PATH = "/src/main/resources/cfr_0_125.jar";

    private Collection<byte[]> innerClasses = new ArrayList<>();

    private Map<String, Object> configuration = getDefaultConfiguration();

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        RuntimeLibraryUploader.appendToClassPath(DECOMPILER_PATH);
        String className = ClassNameUtils.getClassName(byteCode);
        this.innerClasses = classes;

        GetOptParser getOptParser = new GetOptParser();
        Options options = getOptParser.parse(getDefaultOptions(className), OptionsImpl.getFactory());
        DCCommonState dcCommonState = new CFRDCCommonState(options, new ClassFileSourceImpl(options), byteCode);

        ClassFile classFile = dcCommonState.getClassFileMaybePath(className);
        TypeUsageCollector collectingDumper = new TypeUsageCollector(classFile);
        IllegalIdentifierDump illegalIdentifierDump = IllegalIdentifierDump.Factory.get(options);
        dcCommonState.configureWith(classFile);

        classFile.loadInnerClasses(dcCommonState);
        classFile.analyseTop(dcCommonState);
        classFile.collectTypeUsages(collectingDumper);

        Dumper dumper = new CFRBuilderDumper(collectingDumper.getTypeUsageInformation(), options, illegalIdentifierDump);
        classFile.dump(dumper);

        return dumper.toString();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.putAll(configuration.getConfiguration());
    }

    private String[] getDefaultOptions(String className) {
        List<String> options = new ArrayList<>();

        options.add(className);

        for (Map.Entry<String, Object> entry : configuration.entrySet()) {
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
                .dumpClassPathForDebuggingPurposes(true)
                .showDecompilerMessages(true)
                .getConfiguration();
    }

    private class CFRBuilderDumper extends StdIODumper {

        private StringBuilder builder = new StringBuilder();

        private boolean skipLineSeparator = false;

        private CFRBuilderDumper(TypeUsageInformation typeUsageInformation, Options options,
                                 IllegalIdentifierDump illegalIdentifierDump) {
            super(typeUsageInformation, options, illegalIdentifierDump);
        }

        @Override
        protected void write(String data) {
            data = getCorrectData(data);
            builder.append(data);
        }

        private String getCorrectData(String data) {
            if ((data.equals("class ") ||
                    data.equals("interface ") ||
                    data.equals("enum ") ||
                    data.equals("@interface ")) && !skipLineSeparator) {
                skipLineSeparator = true;
            }

            if (data.equals("{")) {
                skipLineSeparator = false;
            }

            if (skipLineSeparator && data.contains("    ")) {
                data = "";
            }

            if (skipLineSeparator && data.contains("\n")) {
                data = " ";
            }

            return data;
        }

        @Override
        public String toString() {
            return correctCode(builder);
        }

        @Override
        public void close() {
            builder.delete(0, builder.length());
        }

        private String correctCode(StringBuilder builder) {
            builder.delete(0, builder.indexOf("*/") + 3);

            int index = 0;
            while (index < builder.length()) {
                if (builder.charAt(index) == '{' && builder.charAt(index - 1) != ' ') {
                    builder.insert(index, ' ');
                }
                index++;
            }

            return builder.toString();
        }
    }

    private class CFRDCCommonState extends DCCommonState {

        private static final String EMPTY_MESSAGE = "";
        private final String outerClassName;
        private ByteCodeCollector codeCollector = new ChainByteCodeCollector();
        private Map<String, ClassFile> classFileMap = new HashMap<>();

        private CFRDCCommonState(Options options, ClassFileSource classFileSource, byte[] byteCode) {
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

        private ClassFile loadClassFileAtPath(String className) {
            if (className.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
                className = className.substring(0, className.lastIndexOf(Constants.Suffix.CLASS_FILE_SUFFIX));
            }

            ClassFile classFile = classFileMap.get(className);
            if (classFile != null) {
                return classFile;
            }

            if (className.contains(outerClassName + Constants.Symbols.DOLLAR)) {
                throw new CannotLoadClassException(EMPTY_MESSAGE, null);
            }

            String name = ClassNameUtils.normalizeFullName(className);
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
}
