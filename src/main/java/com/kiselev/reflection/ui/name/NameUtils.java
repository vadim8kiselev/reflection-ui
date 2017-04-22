package com.kiselev.reflection.ui.name;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        String typeName = "";

        if (clazz.isSynthetic()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        } else {
            typeName = clazz.getSimpleName();
        }

        return typeName;
    }
}
