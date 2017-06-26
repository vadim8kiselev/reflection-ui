package com.kiselev.reflection.ui.impl.packages;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.imports.StateManager;

public class PackageUtils {

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        Class<?> parsedClass = StateManager.getParsedClass();
        if (classPackage != null && clazz.equals(parsedClass)) {

            String packageAnnotations = new AnnotationUtils().getAnnotations(classPackage);
            packageName += packageAnnotations + "package " + classPackage.getName() + ";\n\n";
        }

        return packageName;
    }
}
