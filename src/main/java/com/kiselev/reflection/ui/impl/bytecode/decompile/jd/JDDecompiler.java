package com.kiselev.reflection.ui.impl.bytecode.decompile.jd;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.exception.DecompilationException;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.jd.configuration.JDBuilderConfiguration;
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
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/19/2017.
 *
 * This decompiler doesn't work with java 8
 */
public class JDDecompiler implements Decompiler {

    private ArrayList<ClassFile> innerClasses;

    private Map<String, Object> configuration;

    private static final String fakeName = "FakeClassName.class";

    @Override
    public String decompile(byte[] byteCode) {
        try {
            Loader loader = new JDLoader(byteCode);
            JDPrinter jdPrinter = new JDPrinter(System.out);
            ClassFile classFile = ClassFileDeserializer.Deserialize(loader, fakeName);
            classFile.setInnerClassFiles(innerClasses);

            ReferenceMap referenceMap = new ReferenceMap();
            ClassFileAnalyzer.Analyze(referenceMap, classFile);
            ReferenceAnalyzer.Analyze(referenceMap, classFile);

            CommonPreferences preferences = getCommonPreferences();

            Printer printer = new InstructionPrinter(new PlainTextPrinter(preferences, jdPrinter));

            ArrayList<LayoutBlock> layoutBlockList = new ArrayList<>(1024);
            int maxLineNumber = ClassFileLayouter.Layout(preferences, referenceMap, classFile, layoutBlockList);
            ClassFileWriter.Write(loader, printer, referenceMap, maxLineNumber,
                    classFile.getMajorVersion(), classFile.getMinorVersion(), layoutBlockList);

            return jdPrinter.getSource();
        } catch (ClassFormatException exception) {
            throw new DecompilationException("JD can't decompile classes of java 8", exception);
        } catch (LoaderException | FileNotFoundException exception) {
            throw new DecompilationException("Decompilation process is interrupted", exception);
        }
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration.getConfiguration();
    }

    @Override
    public void appendAdditionalClasses(Collection<byte[]> classes) {
        ArrayList<ClassFile> innerClasses = new ArrayList<>();
        for (byte[] bytecode : classes) {
            try {
                innerClasses.add(ClassFileDeserializer.Deserialize(new JDLoader(bytecode), fakeName));
            } catch (LoaderException exception) {
                throw new DecompilationException("Error loading inner classes", exception);
            }
        }

        this.innerClasses = innerClasses;
    }

    private CommonPreferences getCommonPreferences() {
        if (configuration == null) {
            this.configuration = getDefaultConfiguration();
        } else {
            Map<String, Object> newConfiguration = getDefaultConfiguration();
            newConfiguration.putAll(configuration);
            this.configuration = newConfiguration;
        }

        boolean showDefaultConstructor = (boolean) configuration.get("shc");
        boolean realignmentLineNumber = (boolean) configuration.get("rln");
        boolean showPrefixThis = (boolean) configuration.get("spt");
        boolean mergeEmptyLines = (boolean) configuration.get("mel");
        boolean unicodeEscape = (boolean) configuration.get("uce");
        boolean showLineNumbers = (boolean) configuration.get("sln");

        return new CommonPreferences(showDefaultConstructor,
                realignmentLineNumber,
                showPrefixThis,
                mergeEmptyLines,
                unicodeEscape,
                showLineNumbers);
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

        public JDLoader(byte[] bytecode) {
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

        private StringBuilder builder = new StringBuilder();

        public JDPrinter(OutputStream stream) throws FileNotFoundException {
            super(stream);
        }

        @Override
        public PrintStream append(CharSequence csq) {
            if (isContainsOpenBlock(csq)) {
                int index = getFirstNonSpaceNumber(builder);
                if (builder.charAt(index) == '\n') {
                    builder.deleteCharAt(index);
                }
            } else if (csq.equals("  ")) {
                builder.append(configuration.get("ind"));
                return null;
            } else if (csq.equals("throws") || csq.equals("implements") || csq.equals("extends")) {
                builder.deleteCharAt(getNumberOfLineSeparator(builder));
                builder.delete(getFirstNonSpaceNumber(builder), builder.length() - 1);
            }
            builder.append(csq);
            return null;
        }

        public String getSource() {
            return normalizeOpenBlockCharacter(builder.toString());
        }

        private int getNumberOfLineSeparator(StringBuilder builder) {
            int index = builder.length() - 1;
            while (builder.charAt(index) != '\n') {
                index--;
            }

            return index + 1;
        }

        private int getFirstNonSpaceNumber(StringBuilder builder) {
            int index = builder.length() - 1;
            while (builder.charAt(index) == ' ') {
                index--;
            }

            return index;
        }

        private int getFirstNonSpaceNumber(String line, int number) {
            for (int i = number - 1; i > 0; i--) {
                if (line.charAt(i) != ' ') {
                    return i;
                }
            }

            return -1;
        }

        private boolean isContainsOpenBlock(CharSequence charSequence) {
            int index = charSequence.length() - 1;
            for (int i = 0; i < index; i++) {
                if (charSequence.charAt(index) == '{') {
                    return true;
                }
            }

            return false;
        }

        private String normalizeOpenBlockCharacter(String line) {
            int index = 1;
            while (index != 0) {
                int openBlock = line.indexOf("{", index);
                int nonSpace = getFirstNonSpaceNumber(line, openBlock);
                if (nonSpace != -1 && line.charAt(nonSpace) == '\n') {
                    line = line.substring(0, nonSpace) + " " + line.substring(openBlock, line.length());
                    index = openBlock;
                } else {
                    index = openBlock + 1;
                }
            }

            return line;
        }
    }
}
