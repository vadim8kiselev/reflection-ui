package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.exception.ByteCodeParserException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class ClassFileUtils {

    public static String getFilePath(Class<?> clazz) {
        ClassLoader loader = getClassLoader(clazz);

        if (loader != null) {
            URL resource = loader.getResource(ClassNameUtils.getClassToFileName(clazz));
            if (resource != null) {
                return resource.getFile();
            }
        }

        return null;
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();

        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
            while (loader != null && loader.getParent() != null) {
                loader = loader.getParent();
            }
        }

        return loader;
    }

    public static boolean isArchive(String path) {
        return !getArchiveType(path).isEmpty();
    }

    public static String getClassPackagePath(Class<?> clazz) {
        String path = getFilePath(clazz);

        if (path!= null && !isArchive(path)) {
            return path.substring(0, path.lastIndexOf(Constants.Symbols.SLASH));
        }

        return "";
    }

    public static String getArchivePath(String arFilePath) {
        if (arFilePath!= null && isArchive(arFilePath)) {
            String path = arFilePath.substring(0, getSeparatorPosition(arFilePath))
                    .replace(Constants.Symbols.SLASH, "\\").replace("%20", " ");
            try {
                URL urlPath = new URL(path);
                return urlPath.getFile();
            } catch (MalformedURLException exception) {
                throw new ByteCodeParserException("Jar path: " + path + " is undefined");
            }
        }

        return "";
    }

    public static String getClassNameFromArchivePath(String jarFilePath) {
        if (jarFilePath!= null && isArchive(jarFilePath)) {
            return jarFilePath.substring(getSeparatorPosition(jarFilePath) + 2, jarFilePath.length());
        }

        return "";
    }

    private static int getSeparatorPosition(String jarFilePath) {
        String archiveType = ClassFileUtils.getArchiveType(jarFilePath);
        return jarFilePath.lastIndexOf(Constants.Symbols.DOT + archiveType + "!") + archiveType.length() + 1;
    }

    private static String getArchiveType(String path) {
        if (path.contains(".jar!")) return "jar";
        if (path.contains(".war!")) return "war";
        if (path.contains(".ear!")) return "ear";
        if (path.contains(".zip!")) return "zip";

        return "";
    }
}
