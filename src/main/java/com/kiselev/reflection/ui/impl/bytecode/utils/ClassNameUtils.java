package com.kiselev.reflection.ui.impl.bytecode.utils;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class ClassNameUtils {

    public static String resolveJavaBasedClassName(Class<?> clazz) {
        String className = clazz.getName();
        if (className.contains(Constants.Symbols.SLASH)) {
            className = className.substring(0, className.indexOf(Constants.Symbols.SLASH));
        }

        return className;
    }

    public static String resolveClassFileName(Class<?> clazz) {
        return resolveJavaBasedClassName(clazz).replace(Constants.Symbols.DOT, Constants.Symbols.SLASH)
                + Constants.Suffix.CLASS_FILE_SUFFIX;
    }

    public static String getSimpleName(Class<?> clazz) {
        String typeName = resolveJavaBasedClassName(clazz);
        return typeName.substring(typeName.lastIndexOf(".") + 1);
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
