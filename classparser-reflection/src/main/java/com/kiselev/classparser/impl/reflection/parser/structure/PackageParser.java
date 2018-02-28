package com.kiselev.classparser.impl.reflection.parser.structure;

import com.kiselev.classparser.impl.reflection.parser.base.AnnotationParser;
import com.kiselev.classparser.impl.reflection.state.StateManager;

public class PackageParser {

    private AnnotationParser annotationParser = new AnnotationParser();

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        Class<?> parsedClass = StateManager.getParsedClass();

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (classPackage != null && clazz.equals(parsedClass)) {
            String packageAnnotations = annotationParser.getAnnotations(classPackage);
            packageName += packageAnnotations + "package " +
                    classPackage.getName() + ";" + lineSeparator + lineSeparator;
        }

        return packageName;
    }

    public String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
