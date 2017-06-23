package com.kiselev.reflection.ui.impl.imports;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 06/18/2017.
 */
public class ManagerImportUtils {

    private static Map<Long, ImportUtils> importUtilsMap = new HashMap<>();

    public static synchronized void registerImportUtils(Class<?> clazz) {
        long threadId = Thread.currentThread().getId();
        ImportUtils importUtils = importUtilsMap.get(threadId);

        if (!clazz.isMemberClass() || importUtils == null || importUtils.getParsedClass() == null) {
            importUtilsMap.put(threadId, new ImportUtils(clazz));
        }
    }

    public static synchronized ImportUtils getImportUtils() {
        long threadId = Thread.currentThread().getId();

        if (!importUtilsMap.containsKey(threadId)) {
            throw new RuntimeException("Import utils for current thread is not register");
        }

        return importUtilsMap.get(threadId);
    }
}
