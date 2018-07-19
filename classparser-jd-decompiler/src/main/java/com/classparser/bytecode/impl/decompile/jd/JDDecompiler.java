package com.classparser.bytecode.impl.decompile.jd;

import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;
import com.classparser.bytecode.impl.decompile.jd.configuration.JDBuilderConfiguration;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.bytecode.impl.utils.ClassStringUtils;
import com.classparser.configuration.Configuration;
import com.classparser.configuration.util.ConfigurationUtils;
import com.classparser.exception.decompile.DecompilationException;
import jd.common.preferences.CommonPreferences;
import jd.common.printer.text.PlainTextPrinter;
import jd.core.loader.Loader;
import jd.core.loader.LoaderException;
import jd.core.model.classfile.ClassFile;
import jd.core.model.layout.block.LayoutBlock;
import jd.core.model.reference.ReferenceMap;
import jd.core.printer.InstructionPrinter;
import jd.core.printer.Printer;
import jd.core.process.analyzer.classfile.ClassFileAnalyzer;
import jd.core.process.analyzer.classfile.ReferenceAnalyzer;
import jd.core.process.deserializer.ClassFileDeserializer;
import jd.core.process.deserializer.ClassFormatException;
import jd.core.process.layouter.ClassFileLayouter;
import jd.core.process.writer.ClassFileWriter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Aleksei Makarov on 07/19/2017.
 * <p>
 * This decompiler doesn't work with java 8 and can't display local classes
 */
public final class JDDecompiler implements Decompiler {

    private static final int INITIAL_CAPACITY = 1024;

    private Map<String, Object> configuration;

    private List<ClassFile> innerClasses;

    private ConfigurationUtils utils;

    public JDDecompiler() {
        this.configuration = new HashMap<>();
        this.innerClasses = new ArrayList<>();
    }

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        initUtils();
        try {
            appendAdditionalClasses(classes);
            Loader loader = new JDLoader(byteCode);

            String className = ClassNameUtils.getClassName(byteCode);
            ClassFile classFile = ClassFileDeserializer.Deserialize(loader, className);
            resolveInnerClasses(classFile, innerClasses);

            ReferenceMap referenceMap = new ReferenceMap();
            ClassFileAnalyzer.Analyze(referenceMap, classFile);
            ReferenceAnalyzer.Analyze(referenceMap, classFile);

            CommonPreferences preferences = getCommonPreferences();
            JDPrinter jdPrinter = new JDPrinter(System.out);
            Printer printer = new InstructionPrinter(new PlainTextPrinter(preferences, jdPrinter));

            ArrayList<LayoutBlock> layoutBlockList = new ArrayList<>(INITIAL_CAPACITY);

            int maxLineNumber = ClassFileLayouter.Layout(preferences, referenceMap, classFile, layoutBlockList);
            int minorVersion = classFile.getMinorVersion();
            int majorVersion = classFile.getMajorVersion();

            ClassFileWriter.Write(loader, printer, referenceMap, maxLineNumber,
                    majorVersion, minorVersion, layoutBlockList);

            return jdPrinter.getSource();
        } catch (ClassFormatException | NullPointerException exception) {
            String className = ClassNameUtils.getClassName(byteCode);
            String exceptionMessage = MessageFormat.format("JD can't decompile class: {0}", className);

            throw new DecompilationException(exceptionMessage, exception);
        } catch (LoaderException exception) {
            throw new DecompilationException("Decompilation process is interrupted", exception);
        } catch (Throwable throwable) {
            throw new DecompilationException("Some shit happens with JD decompiler", throwable);
        }
    }

    private void initUtils() {
        if (this.utils == null) {
            this.utils = new ConfigurationUtils(configuration, getDefaultConfiguration());
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.putAll(configuration.getConfiguration());
        this.utils = new ConfigurationUtils(this.configuration, getDefaultConfiguration());
    }

    private void appendAdditionalClasses(Collection<byte[]> classes) {
        List<ClassFile> innerClasses = new ArrayList<>();
        for (byte[] bytecode : classes) {
            try {
                String className = ClassNameUtils.getClassName(bytecode);
                innerClasses.add(ClassFileDeserializer.Deserialize(new JDLoader(bytecode), className));
            } catch (LoaderException exception) {
                throw new DecompilationException("Error loading inner classes", exception);
            }
        }

        this.innerClasses = innerClasses;
    }

    private void resolveInnerClasses(ClassFile classFile, List<ClassFile> innerClasses) {
        String className = ClassNameUtils.normalizeSimpleName(classFile.getThisClassName()) + '$';
        Iterator<ClassFile> iterator = innerClasses.iterator();
        ArrayList<ClassFile> currentInnerClasses = new ArrayList<>();
        while (iterator.hasNext()) {
            ClassFile innerClass = iterator.next();
            String innerClassName = ClassNameUtils.normalizeSimpleName(innerClass.getThisClassName());
            if (!ClassStringUtils.delete(innerClassName, className).contains("$")) {
                innerClass.setOuterClass(classFile);
                currentInnerClasses.add(innerClass);
                iterator.remove();
            }
        }

        classFile.setInnerClassFiles(currentInnerClasses);

        if (!innerClasses.isEmpty()) {
            for (ClassFile currentInnerClass : currentInnerClasses) {
                resolveInnerClasses(currentInnerClass, innerClasses);
            }
        }
    }

    private CommonPreferences getCommonPreferences() {
        return new CommonPreferences(
                utils.getConfig("shc", Boolean.class),
                utils.getConfig("rln", Boolean.class),
                utils.getConfig("spt", Boolean.class),
                utils.getConfig("mel", Boolean.class),
                utils.getConfig("uce", Boolean.class),
                utils.getConfig("sln", Boolean.class)
        );
    }

    private Map<String, Object> getDefaultConfiguration() {
        return JDBuilderConfiguration
                .getBuilderConfiguration()
                .showDefaultConstructor(true)
                .realignmentLineNumber(true)
                .showPrefixThis(true)
                .mergeEmptyLines(true)
                .unicodeEscape(false)
                .showLineNumbers(false)
                .setCountIndentSpaces(4)
                .getConfiguration();
    }

    @Override
    public void setConfigurationManager(ConfigurationManager configurationManager) {
    }

    private class JDLoader implements Loader {

        private final byte[] bytecode;

        private JDLoader(byte[] bytecode) {
            this.bytecode = bytecode;
        }

        @Override
        public DataInputStream load(String dummy) {
            return new DataInputStream(new ByteArrayInputStream(bytecode));
        }

        @Override
        public boolean canLoad(String dummy) {
            return true;
        }
    }

    private class JDPrinter extends PrintStream {

        private static final String JD_INDENT = "  ";

        private final PrintStream stub;

        private final StringBuilder builder;

        private final String indent;

        private JDPrinter(OutputStream stream) {
            super(stream);
            this.indent = utils.getConfig("ind", String.class);
            this.builder = new StringBuilder();
            this.stub = null;
        }

        @Override
        public PrintStream append(CharSequence csq) {
            if (ClassStringUtils.contains(csq, '{')) {
                int index = ClassStringUtils.getFirstLeftNonCharNumber(builder, ' ');
                if (builder.charAt(index) == '\n') {
                    builder.deleteCharAt(index);
                }
            } else if (csq.equals(JD_INDENT)) {
                builder.append(indent);
                return stub;
            } else if (csq.equals("throws") || csq.equals("implements") || csq.equals("extends")) {
                builder.deleteCharAt(ClassStringUtils.getNumberLeftOfLineSeparator(builder));
                builder.delete(ClassStringUtils.getFirstLeftNonCharNumber(builder, ' '), builder.length() - 1);
            }
            builder.append(csq);

            return stub;
        }

        private String getSource() {
            return ClassStringUtils.normalizeOpenBlockCharacter(builder);
        }
    }
}