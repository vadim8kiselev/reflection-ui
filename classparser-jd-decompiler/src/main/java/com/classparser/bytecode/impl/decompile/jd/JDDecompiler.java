package com.classparser.bytecode.impl.decompile.jd;

import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
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

import java.io.*;
import java.util.*;

/**
 * Created by Aleksei Makarov on 07/19/2017.
 * <p>
 * This decompiler doesn't work with java 8 and can't show local classes
 */
public final class JDDecompiler implements Decompiler {

    private static final int INITIAL_CAPACITY = 1024;

    private List<ClassFile> innerClasses = new ArrayList<>();

    private Map<String, Object> configuration = getDefaultConfiguration();

    private ConfigurationUtils utils;

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        if (this.utils == null) {
            this.utils = new ConfigurationUtils(configuration, getDefaultConfiguration());
        }

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

            ClassFileWriter.Write(loader, printer, referenceMap, maxLineNumber, majorVersion, minorVersion, layoutBlockList);

            return jdPrinter.getSource();
        } catch (ClassFormatException | NullPointerException exception) {
            String className = ClassNameUtils.getClassName(byteCode);
            String exceptionMessage = String.format("JD can't decompile class: %s", className);

            throw new DecompilationException(exceptionMessage, exception);
        } catch (LoaderException | FileNotFoundException exception) {
            throw new DecompilationException("Decompilation process is interrupted", exception);
        } catch (Throwable throwable) {
            throw new DecompilationException("Some shit happens with JD decompiler", throwable);
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
        String className = ClassNameUtils.normalizeSimpleName(classFile.getThisClassName()) + Constants.Symbols.DOLLAR;
        Iterator<ClassFile> iterator = innerClasses.iterator();
        ArrayList<ClassFile> currentInnerClasses = new ArrayList<>();
        while (iterator.hasNext()) {
            ClassFile innerClass = iterator.next();
            String innerClassName = ClassNameUtils.normalizeSimpleName(innerClass.getThisClassName());
            if (!ClassStringUtils.delete(innerClassName, className).contains(Constants.Symbols.DOLLAR)) {
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

    private class JDLoader implements Loader {

        private byte[] bytecode;

        private JDLoader(byte[] bytecode) {
            this.bytecode = bytecode;
        }

        @Override
        public DataInputStream load(String dummy) throws LoaderException {
            return new DataInputStream(new ByteArrayInputStream(bytecode));
        }

        @Override
        public boolean canLoad(String dummy) {
            return true;
        }
    }

    private class JDPrinter extends PrintStream {

        private static final String JD_INDENT = "  ";
        private final PrintStream STUB = null;
        private StringBuilder builder = new StringBuilder();
        private String indent;

        private JDPrinter(OutputStream stream) throws FileNotFoundException {
            super(stream);
            this.indent = utils.getConfig("ind", String.class);
        }

        @Override
        public PrintStream append(CharSequence csq) {
            if (ClassStringUtils.contains(csq)) {
                int index = ClassStringUtils.getFirstLeftNonCharNumber(builder, ' ');
                if (builder.charAt(index) == '\n') {
                    builder.deleteCharAt(index);
                }
            } else if (csq.equals(JD_INDENT)) {
                builder.append(indent);
                return STUB;
            } else if (csq.equals("throws") || csq.equals("implements") || csq.equals("extends")) {
                builder.deleteCharAt(ClassStringUtils.getNumberLeftOfLineSeparator(builder));
                builder.delete(ClassStringUtils.getFirstLeftNonCharNumber(builder, ' '), builder.length() - 1);
            }
            builder.append(csq);
            return STUB;
        }

        private String getSource() {
            return ClassStringUtils.normalizeOpenBlockCharacter(builder);
        }
    }
}
