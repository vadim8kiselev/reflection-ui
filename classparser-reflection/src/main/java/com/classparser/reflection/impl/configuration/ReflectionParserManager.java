package com.classparser.reflection.impl.configuration;

public class ReflectionParserManager {

    private Class<?> parsedClass;

    private Class<?> currentParsedClass;

    private ConfigurationManager configurationManager;

    public ReflectionParserManager() {
        this.configurationManager = new ConfigurationManager();
    }

    public Class<?> getParsedClass() {
        return parsedClass;
    }

    public void setParsedClass(Class<?> parsedClass) {
        this.parsedClass = parsedClass;
    }

    public Class<?> getCurrentParsedClass() {
        return currentParsedClass;
    }

    public void setCurrentParsedClass(Class<?> currentParsedClass) {
        this.currentParsedClass = currentParsedClass;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void popCurrentClass() {
        if (getCurrentParsedClass() != null) {
            setCurrentParsedClass(getCurrentParsedClass().getDeclaringClass());
        }
    }

    public void clearState() {
        parsedClass = null;
        currentParsedClass = null;
    }
}
