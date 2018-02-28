package com.kiselev.classparser.impl.reflection.parser;

public class ClassTypeParser {

    public String getType(Class<?> clazz) {
        String type = "class ";

        if (clazz.isEnum()) {
            type = "enum ";
        } else if (clazz.isInterface()) {
            type = "interface ";
        } else if (clazz.isAnnotation()) {
            type = "@interface ";
        }

        return type;
    }
}
