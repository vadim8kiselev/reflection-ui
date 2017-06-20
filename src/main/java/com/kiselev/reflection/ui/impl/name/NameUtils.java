package com.kiselev.reflection.ui.impl.name;

import com.kiselev.reflection.ui.impl.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = ManagerImportUtils.getImportUtils();
        String typeName;

        //TODO : add resolve name and imports inner classes

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
}
