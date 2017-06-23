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
    public void saveClassFile(String classFile, String javaFilename, String s2, String sourceCode, int[] ints) {
        this.sourceCode = sourceCode;
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
        String ZERO = "0";
        String ONE = "1";
        return new HashMap<String, Object>() {
            {
                put("rbr", ZERO);          //show bridge methods
                put("rsy", ZERO);          //show synthetic class members
                put("din", ONE);          //decompile inner classes
                put("dc4", ZERO);          //collapse class references
                put("das", ONE);          //decompile assertions
                put("hes", ZERO);          //show empty super invocation
                put("hdc", ZERO);          //show empty default constructor
                put("dgs", ONE);          //decompile generic signatures
                put("ner", ZERO);          //assume return not throwing exceptions
                put("den", ONE);          //decompile enumerations
                put("rgn", ZERO);          //show getClass() invocation, when it is part of a qualified new statement
                put("lit", ONE);          //show output numeric literals "as-is"
                put("asc", ZERO);          //for encode non-ASCII characters
                put("bto", ZERO);          //don't interpret int 1 as boolean true
                put("nns", ZERO);          //allow for set synthetic attribute
                put("uto", ONE);          //consider nameless types as java.lang.Object
                put("udv", ONE);          //reconstruct variable names from debug information, if present
                put("rer", ZERO);          //don't remove empty exception ranges
                put("fdi", ONE);          //de-inline finally structures
                put("mpm", ZERO);          //don't upper limit for decompilation
                put("ren", ZERO);          //don't rename ambiguous classes and class elements
                put("inn", ZERO);          //don't check for IntelliJ IDEA-specific @NotNull annotation
                put("lac", ZERO);          //don't decompile lambda expressions to anonymous classes
                put("ind", "    ");       //indent spaces
                put("log", "ERROR");      //log lever
            }
        };
    }
}
