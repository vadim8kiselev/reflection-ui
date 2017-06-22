package com.kiselev.reflection.ui.impl.name;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.imports.ImportUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;

public class NameUtils {

    public String getTypeName(Class<?> clazz) {
        ImportUtils importUtils = ManagerImportUtils.getImportUtils();

        return importUtils.addImport(clazz) ? getSimpleName(clazz) : clazz.getName();
    }

    public String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf(Constants.Symbols.POINT) + 1);
        }

        return typeName;
    }
}
