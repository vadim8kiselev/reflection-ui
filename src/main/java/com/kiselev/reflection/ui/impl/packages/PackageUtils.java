package com.kiselev.reflection.ui.impl.packages;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;

public class PackageUtils {

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        if (classPackage != null && clazz.getDeclaringClass() == null) {
            packageName += new AnnotationUtils().getAnnotations(classPackage)
                    + "package " + classPackage.getName() + ";\n\n";
        }

        return packageName;
    }
}
