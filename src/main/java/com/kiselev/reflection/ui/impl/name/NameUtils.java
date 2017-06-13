package com.kiselev.reflection.ui.impl.name;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();

        if ("".equals(typeName)) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }

        return typeName;
    }
}
