package com.kiselev.reflection.ui.impl.packages;

public class PackageUtils {

    public String getPackage(Class<?> clazz) {
        String packageName = "";

        if (clazz.getPackage() != null && clazz.getDeclaringClass() == null) {
            packageName += "package " + clazz.getPackage().getName() + ";\n\n";
        }

        return packageName;
    }
}
