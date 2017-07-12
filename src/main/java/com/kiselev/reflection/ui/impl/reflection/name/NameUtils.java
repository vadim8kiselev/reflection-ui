package com.kiselev.reflection.ui.impl.reflection.name;

import com.kiselev.reflection.ui.impl.reflection.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;

import java.lang.reflect.Member;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = StateManager.getImportUtils();
        return importUtils.addImport(clazz) ? getSimpleName(clazz) : clazz.getName();
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        }
        return typeName;
    }

    public String getMemberName(Member member) {
        return member.getName();
    }
}
