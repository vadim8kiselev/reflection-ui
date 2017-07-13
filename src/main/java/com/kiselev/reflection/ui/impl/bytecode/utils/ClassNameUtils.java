package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class ClassNameUtils {

    public static String getJavaBasedClassName(Class<?> clazz) {
        String className = clazz.getName();
        if (className.contains(Constants.Symbols.SLASH)) {
            className = className.substring(0, className.indexOf(Constants.Symbols.SLASH));
        }

        return className;
    }

    public static String normalizeSimpleName(String className) {
        String fullName = normalizeFullName(className);
        return fullName.substring(fullName.lastIndexOf(Constants.Symbols.DOT) + 1);
    }

    public static String normalizeFullName(String className) {
        if (className.endsWith(Constants.Suffix.CLASS_FILE_SUFFIX)) {
            className = className.replace(Constants.Suffix.CLASS_FILE_SUFFIX, "");
        }

        return className.replace(Constants.Symbols.SLASH, Constants.Symbols.DOT);
    }

    public static String getClassToFileName(Class<?> clazz) {
        return getJavaBasedClassName(clazz).replace(Constants.Symbols.DOT, Constants.Symbols.SLASH)
                + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    public static String getSimpleName(Class<?> clazz) {
        String typeName = getJavaBasedClassName(clazz);
        return typeName.substring(typeName.lastIndexOf(Constants.Symbols.DOT) + 1);
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
