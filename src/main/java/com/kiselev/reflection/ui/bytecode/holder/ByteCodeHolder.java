package com.kiselev.reflection.ui.bytecode.holder;

import com.kiselev.reflection.ui.bytecode.assembly.AgentAssembler;
import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.bytecode.decompile.Decompiler;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class ByteCodeHolder {

    private static Map<String, byte[]> byteCodeMap = new HashMap<>();

    private static Instrumentation instrumentation;

    public static void uploadByteCodeForClass(String className, byte[] byteCode) {
        byteCodeMap.put(className, byteCode);
    }

    public static String getDecompiledByteCode(Class<?> clazz) {
        if (!AgentAssembler.isAssembled()) {
            AgentAssembler.assembly();
        }

        retransformClass(clazz);

        String classFileName = getClassFileName(clazz);
        String javaBasedClassName = getJavaBasedClassName(clazz);
        byte[] byteCode = byteCodeMap.get(javaBasedClassName);
        writeByteCodeToFile(classFileName, byteCode);

        Decompiler decompiler = new Decompiler();
        return decompiler.decompile(classFileName, byteCode);
    }

    public static void registerInstrumentation(Instrumentation instrumentation) {
        ByteCodeHolder.instrumentation = instrumentation;
    }

    private static void retransformClass(Class<?> clazz) {
        try {
            if (instrumentation != null) {
                instrumentation.retransformClasses(clazz);
            }
        } catch (UnmodifiableClassException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String getClassFileName(Class<?> clazz) {
        String classFileName = "classes" + File.separator + getJavaBasedClassName(clazz)
                .replace(Constants.Symbols.DOT, File.separator);
        createClassFileNameDirectory(classFileName); // todo
        return classFileName + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    // TODO : CHECK
    private static void createClassFileNameDirectory(String classFileName) {
        String path = System.getProperty(Constants.Properties.HOME_DIR) + File.separator
                + classFileName.substring(0, classFileName.lastIndexOf(File.separator));
        Path directoryPath = Paths.get(path);
        try {
            Files.createDirectories(directoryPath).toFile();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void writeByteCodeToFile(String fileName, byte[] byteCode) {
        if (fileName != null && byteCode != null) {
            try (FileOutputStream stream = new FileOutputStream(fileName.replace(Constants.Symbols.DOLLAR, ""))) {
                stream.write(byteCode);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            throw new RuntimeException("Empty file name or byte code");
        }
    }

    private static String getJavaBasedClassName(Class<?> clazz) {
        String className = clazz.getName();
        if (className.contains(Constants.Symbols.SLASH)) {
            className = className.substring(0, className.indexOf(Constants.Symbols.SLASH));
        }

        return className;
    }
}
