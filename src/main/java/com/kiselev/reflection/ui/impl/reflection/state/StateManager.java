package com.kiselev.reflection.ui.impl.reflection.state;

import com.kiselev.reflection.ui.exception.ReflectionParserException;
import com.kiselev.reflection.ui.impl.reflection.configuration.ConfigurationManager;
import com.kiselev.reflection.ui.impl.reflection.imports.ImportUtils;

import java.util.Map;

/**
 * Created by Aleksei Makarov on 06/18/2017.
 */
public class StateManager {

    private static ThreadLocal<ImportUtils> importUtilsMap = new ThreadLocal<>();

    private static ThreadLocal<Class<?>> parsedClass = new ThreadLocal<>();

    private static ThreadLocal<Class<?>> currentClass = new ThreadLocal<>();

    private static ThreadLocal<ConfigurationManager> configuration = new ThreadLocal<>();

    public static void registerImportUtils(Class<?> clazz) {
        if (!clazz.isMemberClass() || importUtilsMap.get() == null || currentClass.get() == null) {
            parsedClass.set(clazz);
            currentClass.set(clazz);
            importUtilsMap.set(new ImportUtils());
            if (configuration.get() == null) {
                configuration.set(new ConfigurationManager());
            }
        }
    }

    public static ImportUtils getImportUtils() {
        ImportUtils importUtils = importUtilsMap.get();
        if (importUtils == null) {
            throw new ReflectionParserException("Import utils for current thread is not register");
        }

        return importUtils;
    }

    public static void clearState() {
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

    public static ConfigurationManager getConfiguration() {
        ConfigurationManager configurationManager = configuration.get();
        if (configurationManager == null) {
            configuration.set(new ConfigurationManager());
        }

        return configuration.get();
    }

    public static void setConfiguration(Map<String, Object> configuration) {
        StateManager.configuration.set(new ConfigurationManager(configuration));
    }
}
