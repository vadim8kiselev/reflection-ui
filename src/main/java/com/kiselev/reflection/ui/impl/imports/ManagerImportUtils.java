package com.kiselev.reflection.ui.impl.imports;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 18.06.2017.
 */
public class ManagerImportUtils {

    private static Map<Long, ImportUtils> importUtilsMap = new HashMap<>();

    public static void registerImportUtils(Class<?> clazz) {
        long id = Thread.currentThread().getId();
        ImportUtils importUtils = importUtilsMap.get(id);
        if (!clazz.isMemberClass() || clazz.isMemberClass() && (importUtils == null || importUtils.getParsedClass() == null)) {
            importUtilsMap.put(id, new ImportUtils(clazz));
        }
    }

    public static ImportUtils getImportUtils() {
        long id = Thread.currentThread().getId();
        if (!importUtilsMap.containsKey(id)) {
            throw new RuntimeException("Import utils for current thread is not register");
        }

        return importUtilsMap.get(id);
    }
}
