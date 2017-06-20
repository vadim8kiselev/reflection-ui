package com.kiselev.reflection.ui.impl.name;

import com.kiselev.reflection.ui.impl.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = ManagerImportUtils.getImportUtils();
        String typeName;
        Class<?> externalClass;

        if (clazz.isMemberClass()) {    //TODO : add resolve name and imports inner classes
            externalClass = getTopClass(importUtils.getParsedClass());

        }

        if (importUtils.addImport(clazz)) {
            typeName = getSimpleName(clazz);
        } else {
            typeName = clazz.getName();
        }

        return typeName;
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if ("".equals(typeName)) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }

        return typeName;
    }

    private Class<?> getTopClass(Class<?> clazz) {
        if (clazz.getDeclaringClass() == null) {
            return clazz;
        } else {
            return getTopClass(clazz.getDeclaringClass());
        }
    }
}
