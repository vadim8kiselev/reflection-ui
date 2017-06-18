package com.kiselev.reflection.ui.impl.imports;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 18.06.2017.
 */
public class ManagerImportUtils {

    private static Map<Class<?>, ImportUtils> importUtilsMap = new HashMap<>();

    public static ImportUtils getImportUtils(Class<?> clazz) {
        while (clazz.getDeclaringClass() != null) {
            clazz = clazz.getDeclaringClass();
        }

        if (!importUtilsMap.containsKey(clazz)) {
            importUtilsMap.put(clazz, new ImportUtils(clazz));
        }

        return importUtilsMap.get(clazz);
    }
}
