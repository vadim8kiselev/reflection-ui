package com.classparser.reflection.impl.state;

import com.classparser.exception.ReflectionParserException;
import com.classparser.reflection.impl.configuration.ConfigurationManager;
import com.classparser.reflection.impl.parser.imports.ImportParser;

import java.util.Map;

public class StateManager {

    private static final ThreadLocal<State> states = new ThreadLocal<>();

    public static void registerImportUtils(Class<?> clazz) {
        State state = getCurrentState();
        if (!clazz.isMemberClass() ||
                state == null ||
                state.getImportParser() == null ||
                state.getCurrentParsedClass() == null) {
            state = new State(new ImportParser(), clazz, clazz);
            setCurrentState(state);
            if (state.getConfigurationManager() == null) {
                state.setConfigurationManager(new ConfigurationManager());
            }
        }
    }

    public static ImportParser getImportUtils() {
        State state = getCurrentState();
        ImportParser importParser = state.getImportParser();
        if (importParser == null) {
            throw new ReflectionParserException("Import utils is not register");
        }

        return importParser;
    }

    public static void clearState() {
        getCurrentState().clearState();
    }

    public static Class<?> getParsedClass() {
        return getCurrentState().getMainParsedClass();
    }

    public static Class<?> getCurrentClass() {
        return getCurrentState().getCurrentParsedClass();
    }

    public static void setCurrentClass(Class<?> currentClass) {
        getCurrentState().setCurrentParsedClass(currentClass);
    }

    public static void popCurrentClass() {
        State state = getCurrentState();
        if (state.getCurrentParsedClass() != null) {
            state.setCurrentParsedClass(state.getCurrentParsedClass().getDeclaringClass());
        }
    }

    public static ConfigurationManager getConfiguration() {
        ConfigurationManager configurationManager = getCurrentState().getConfigurationManager();
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
            getCurrentState().setConfigurationManager(configurationManager);
        }

        return configurationManager;
    }

    public static void setConfiguration(Map<String, Object> configuration) {
        getCurrentState().setConfigurationManager(new ConfigurationManager(configuration));
    }

    private static State getCurrentState() {
        return states.get();
    }

    private static void setCurrentState(State state) {
        states.set(state);
    }

}
