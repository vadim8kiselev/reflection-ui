package com.classparser.reflection.impl.parser;

import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.Member;

public class ClassNameParser {

    public static String getTypeName(Class<?> clazz) {
        return StateManager.getImportUtils().addImport(clazz) ? getSimpleName(clazz) : getName(clazz);
    }

    public static String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        }
        return typeName;
    }

    public static String getName(Class<?> clazz) {
        if (clazz.isMemberClass() || clazz == StateManager.getParsedClass()) {
            return getSimpleName(clazz);
        } else {
            return clazz.getName();
        }
    }

    public static String getMemberName(Member member) {
        return member.getName();
    }
}
