package com.kiselev.reflection.ui.impl.name;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        String typeName = "";

        if (clazz.isSynthetic() || clazz.getName().contains("$")) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        } else {
            typeName = clazz.getSimpleName();
        }

        return typeName;
    }
}
