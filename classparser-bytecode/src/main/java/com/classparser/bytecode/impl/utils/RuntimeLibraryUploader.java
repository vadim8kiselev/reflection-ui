package com.classparser.bytecode.impl.utils;

import com.classparser.exception.ByteCodeParserException;
import com.classparser.exception.file.ReadFileException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class RuntimeLibraryUploader {

    public static void appendToClassPath(String libraryName) {
        URL systemResource = ClassLoader.getSystemResource(libraryName);
        String filePath = systemResource.getFile();
        File file = new File(filePath);
        if (file.exists()) {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            if (systemClassLoader instanceof URLClassLoader) {
                try {
                    URL libraryURL = file.toURI().toURL();
                    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                    method.setAccessible(true);
                    method.invoke(systemClassLoader, libraryURL);
                } catch (Exception exception) {
                    throw new ReadFileException("Library by path: " + libraryName + " is can't be uploaded!", exception);
                }
            } else {
                String classLoaderName = systemClassLoader.getClass().getName();
                throw new ByteCodeParserException("System class loader: " + classLoaderName + " is not URLClassLoader!");
            }
        } else {
            throw new ReadFileException("Library by path: " + libraryName + " is not found!");
        }
    }
}
