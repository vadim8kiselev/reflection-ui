package com.classparser.bytecode.impl.utils;

import com.classparser.exception.ByteCodeParserException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class ClassFileUtils {

    private static final String SHIELDED_SPACE = "%20";

    private static final String PROTOCOL = "file://";

    public static String getFilePath(Class<?> clazz) {
        ClassLoader loader = getClassLoader(clazz);

        if (loader != null && clazz != null) {
            URL resource = loader.getResource(ClassNameUtils.getClassToJarFileName(clazz));
            if (resource != null) {
                return resource.getFile();
            }
        }

        return "";
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = null;

        if (clazz != null) {
            loader = clazz.getClassLoader();

            if (loader == null) {
                loader = ClassLoader.getSystemClassLoader();
                while (loader != null && loader.getParent() != null) {
                    loader = loader.getParent();
                }
            }
        }

        return loader;
    }

    public static boolean isArchive(String path) {
        return !getArchiveType(path).isEmpty();
    }

    public static String getClassPackagePath(Class<?> clazz) {
        String path = getFilePath(clazz);

        if (!path.isEmpty() && !isArchive(path)) {
            return path.substring(0, path.lastIndexOf('/'));
        }

        return "";
    }

    public static String getArchivePath(String jarFilePath) {
        if (!jarFilePath.isEmpty() && isArchive(jarFilePath)) {
            String path = jarFilePath.substring(0, getSeparatorPosition(jarFilePath));
            if ('/' != File.separatorChar) {
                path = path.replace('/', File.separatorChar);
            }

            path = PROTOCOL + path.replace(SHIELDED_SPACE, " ");
            try {
                URL urlPath = new URL(path);
                return urlPath.getFile();
            } catch (MalformedURLException exception) {
                String message = MessageFormat.format("Jar path: {0} is undefined", path);
                throw new ByteCodeParserException(message, exception);
            }
        }

        return "";
    }

    public static String getClassNameFromArchivePath(String jarFilePath) {
        if (jarFilePath != null && isArchive(jarFilePath)) {
            return jarFilePath.substring(getSeparatorPosition(jarFilePath) + 2, jarFilePath.length());
        }

        return "";
    }

    private static int getSeparatorPosition(String jarFilePath) {
        String archiveType = ClassFileUtils.getArchiveType(jarFilePath);
        return jarFilePath.lastIndexOf('.' + archiveType + '!') + archiveType.length() + 1;
    }

    private static String getArchiveType(String path) {
        if (path.contains(".jar!")) {
            return "jar";
        }
        if (path.contains(".war!")) {
            return "war";
        }
        if (path.contains(".ear!")) {
            return "ear";
        }
        if (path.contains(".zip!")) {
            return "zip";
        }

        return "";
    }
}