package com.kiselev.reflection.ui.impl.name;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

import java.util.Arrays;
import java.util.List;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = ManagerImportUtils.getImportUtils();
        String typeName;

        if (clazz.isMemberClass()) {
            if (!getTopClass(clazz).equals(getTopClass(importUtils.getParsedClass())) || !isInVisibilityZone(clazz)) {
                typeName = new GenericsUtils().resolveType(clazz.getEnclosingClass()) + "." + getSimpleName(clazz);
                return typeName;
            }
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
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }

        return typeName;
    }

    private Class<?> getTopClass(Class<?> innerClass) {
        if (innerClass.getDeclaringClass() == null) {
            return innerClass;
        } else {
            return getTopClass(innerClass.getDeclaringClass());
        }
    }

    private boolean isInVisibilityZone(Class<?> clazz) {
        Class<?> currentClass = ManagerImportUtils.getImportUtils().getCurrentClass();
        while (currentClass != null) {
            List<Class<?>> innerClasses = Arrays.asList(currentClass.getDeclaredClasses());
            if (innerClasses.contains(clazz)) {
                return true;
            }

            currentClass = currentClass.getDeclaringClass();
        }

        return false;
    }
}
