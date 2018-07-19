package com.classparser.reflection.impl.parser;

public class ClassTypeParser {

    public String getType(Class<?> clazz) {
        String type = "class ";

        if (clazz.isEnum()) {
            type = "enum ";
        } else if (clazz.isAnnotation()) {
            type = "@interface ";
        } else if (clazz.isInterface()) {
            type = "interface ";
        }

        return type;
    }
}