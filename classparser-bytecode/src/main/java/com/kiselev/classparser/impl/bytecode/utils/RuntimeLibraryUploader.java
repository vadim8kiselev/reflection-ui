package com.kiselev.classparser.impl.bytecode.utils;

import com.kiselev.classparser.exception.file.ReadFileException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class RuntimeLibraryUploader {

    public static void appendToClassPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            try {
                URL libraryURL = file.toURI().toURL();
                Method method = systemClassLoader.getClass().getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(systemClassLoader, libraryURL);
            } catch (Exception exception) {
                throw new ReadFileException("Library by path: " + path + " is can't be uploaded!");
            }
        } else {
            throw new ReadFileException("Library by path: " + path + " is not found!");
        }
    }
}
