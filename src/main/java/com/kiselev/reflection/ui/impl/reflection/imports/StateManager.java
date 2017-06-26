package com.kiselev.reflection.ui.impl.reflection.imports;

/**
 * Created by Aleksei Makarov on 06/18/2017.
 */
public class StateManager {

    private static ThreadLocal<ImportUtils> importUtilsMap = new ThreadLocal<>();

    private static ThreadLocal<Class<?>> parsedClass = new ThreadLocal<>();

    private static ThreadLocal<Class<?>> currentClass = new ThreadLocal<>();

    public static void registerImportUtils(Class<?> clazz) {
        ImportUtils importUtils = importUtilsMap.get();

        if (!clazz.isMemberClass() || importUtils == null || importUtils.isCleared()) {
            parsedClass.set(clazz);
            currentClass.set(clazz);
            importUtilsMap.set(new ImportUtils());
        }
    }

    public static ImportUtils getImportUtils() {
        if (importUtilsMap.get() == null) {
            throw new RuntimeException("Import utils for current thread is not register");
        }

        return importUtilsMap.get();
    }

    static void clearState() {
        parsedClass.set(null);
        currentClass.set(null);
        importUtilsMap.set(null);
    }

    public static Class<?> getParsedClass() {
        return parsedClass.get();
    }

    public static void setCurrentClass(Class<?> currentClass) {
        StateManager.currentClass.set(currentClass);
    }

    public static Class<?> getCurrentClass() {
        return currentClass.get();
    }

    public static void popCurrentClass() {
        if (currentClass != null && currentClass.get() != null) {
            currentClass.set(currentClass.get().getDeclaringClass());
        }
    }
}
