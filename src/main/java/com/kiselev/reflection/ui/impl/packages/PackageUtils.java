package com.kiselev.reflection.ui.impl.packages;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

public class PackageUtils {

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        Package classPackage = clazz.getPackage();
        if (classPackage != null && clazz.equals(ManagerImportUtils.getImportUtils().getParsedClass())) {
            packageName += new AnnotationUtils().getAnnotations(classPackage)
                    + "package " + classPackage.getName() + ";\n\n";
        }

        return packageName;
    }
}
