package com.kiselev.reflection.ui.impl.reflection.name;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.reflection.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.reflection.imports.StateManager;

import java.lang.reflect.Member;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = StateManager.getImportUtils();
        return importUtils.addImport(clazz) ? getSimpleName(clazz) : getName(clazz);
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf(Constants.Symbols.DOT) + 1);
        }
        return typeName;
    }

    public String getName(Class<?> clazz) {
        String className = "";

        if (clazz.getPackage() == null) {
            className += "root.";
        }

        return className + clazz.getName();
    }

    public String getMemberName(Member member) {
        return member.getName();
    }
}
