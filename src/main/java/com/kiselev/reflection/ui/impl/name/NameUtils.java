package com.kiselev.reflection.ui.impl.name;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = ManagerImportUtils.getImportUtils();
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
}
