package com.kiselev.classparser.impl.reflection.name;

import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.Member;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        return StateManager.getImportUtils().addImport(clazz) ? getSimpleName(clazz) : getName(clazz);
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        }
        return typeName;
    }

    public String getName(Class<?> clazz) {
        if (clazz.isMemberClass() || clazz == StateManager.getParsedClass()) {
            return getSimpleName(clazz);
        } else {
            return clazz.getName();
        }
    }

    public String getMemberName(Member member) {
        return member.getName();
    }
}
