package com.kiselev.reflection.ui.bytecode.decompile;

import org.jetbrains.java.decompiler.main.decompiler.BaseDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * Created by Aleksei Makarov on 23.06.2017.
 */
public class Decompiler implements IResultSaver, IBytecodeProvider {

    private byte[] byteCode;

    private String sourceCode;

    public String decompile(String fileName, byte[] byteCode) {
        this.byteCode = byteCode;
        this.sourceCode = "";

        try {
            IFernflowerLogger logger = new PrintStreamLogger(System.out);
            BaseDecompiler decompiler = new BaseDecompiler(this, this, getConfiguration(), logger);
            decompiler.addSpace(new File(fileName), true);
            decompiler.decompileContext();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return sourceCode;
    }

    @Override
    public byte[] getBytecode(String s, String s1) throws IOException {
        return byteCode;
    }

    @Override
    public void saveFolder(String folder) {
        //empty
    }

    @Override
    public void copyFile(String content, String s1, String s2) {
        //empty
    }

    @Override
    public void saveClassFile(String classFile, String javaFilename, String s2, String content, int[] ints) {
        this.sourceCode = content;
    }

    @Override
    public void createArchive(String content, String s1, Manifest manifest) {
        //empty
    }

    @Override
    public void saveDirEntry(String content, String s1, String s2) {
        //empty
    }

    @Override
    public void copyEntry(String content, String s1, String s2, String s3) {
        //empty
    }

    @Override
    public void saveClassEntry(String content, String s1, String s2, String s3, String s4) {
        //empty
    }

    @Override
    public void closeArchive(String content, String s1) {
        //empty
    }

    private Map<String, Object> getConfiguration() {
        return new HashMap<String, Object>() {
            {
                put("rbr", "0");          //show bridge methods
                put("rsy", "0");          //show synthetic class members
                put("din", "1");          //decompile inner classes
                put("dc4", "0");          //collapse class references
                put("das", "1");          //decompile assertions
                put("hes", "0");          //show empty super invocation
                put("hdc", "0");          //show empty default constructor
                put("dgs", "1");          //decompile generic signatures
                put("ner", "0");          //assume return not throwing exceptions
                put("den", "1");          //decompile enumerations
                put("rgn", "0");          //show getClass() invocation, when it is part of a qualified new statement
                put("lit", "1");          //show output numeric literals "as-is"
                put("asc", "0");          //for encode non-ASCII characters
                put("bto", "0");          //don't interpret int 1 as boolean true
                put("nns", "0");          //allow for set synthetic attribute
                put("uto", "1");          //consider nameless types as java.lang.Object
                put("udv", "1");          //reconstruct variable names from debug information, if present
                put("rer", "0");          //don't remove empty exception ranges
                put("fdi", "1");          //de-inline finally structures
                put("mpm", "0");          //don't upper limit for decompilation
                put("ren", "0");          //don't rename ambiguous classes and class elements
                put("inn", "0");          //don't check for IntelliJ IDEA-specific @NotNull annotation
                put("lac", "0");          //don't decompile lambda expressions to anonymous classes
                put("ind", "    ");       //indent spaces
                put("log", "ERROR");      //log lever
            }
        };
    }
}
