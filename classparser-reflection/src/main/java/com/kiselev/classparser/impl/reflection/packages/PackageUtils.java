package com.kiselev.classparser.impl.reflection.packages;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;

public class PackageUtils {

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        Class<?> parsedClass = StateManager.getParsedClass();

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (classPackage != null && clazz.equals(parsedClass)) {
            String packageAnnotations = annotationUtils.getAnnotations(classPackage);
            packageName += packageAnnotations + "package " +
                    classPackage.getName() + ";" + lineSeparator + lineSeparator;
        }

        return packageName;
    }

    public String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
