package com.kiselev.reflection.ui.type;

public class TypeUtils {

    public String getType(Class<?> clazz) {
        String type = "class ";

        if (clazz.isEnum()) type = "enum ";
        if (clazz.isInterface()) type = "interface ";
        if (clazz.isAnnotation()) type = "@interface ";

        return type;
    }
}
