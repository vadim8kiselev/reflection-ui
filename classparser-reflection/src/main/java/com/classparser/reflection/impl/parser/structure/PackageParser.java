package com.classparser.reflection.impl.parser.structure;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.state.StateManager;

public class PackageParser {

    public static String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        Class<?> parsedClass = StateManager.getParsedClass();

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (classPackage != null && clazz.equals(parsedClass)) {
            String packageAnnotations = AnnotationParser.getAnnotations(classPackage);
            packageName += packageAnnotations + "package " +
                    classPackage.getName() + ";" + lineSeparator + lineSeparator;
        }

        return packageName;
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
