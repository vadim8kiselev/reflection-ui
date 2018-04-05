package com.classparser.reflection.impl.parser.structure;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.state.ReflectionParserManager;

public class PackageParser {

    private final AnnotationParser annotationParser;

    private final ReflectionParserManager manager;

    public PackageParser(AnnotationParser annotationParser, ReflectionParserManager manager) {
        this.annotationParser = annotationParser;
        this.manager = manager;
    }

    public static String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        Class<?> parsedClass = manager.getParsedClass();

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (classPackage != null && clazz.equals(parsedClass)) {
            String packageAnnotations = annotationParser.getAnnotations(classPackage);
            packageName += packageAnnotations + "package " +
                    classPackage.getName() + ';' + lineSeparator + lineSeparator;
        }

        return packageName;
    }
}
