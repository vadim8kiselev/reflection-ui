package com.classparser.reflection.impl.configuration;

public class ReflectionParserManager {

    private final ThreadLocal<Class<?>> threadLocalParsedClass;

    private final ThreadLocal<Class<?>> threadLocalCurrentParsedClass;

    private final ConfigurationManager configurationManager;

    public ReflectionParserManager() {
        this.configurationManager = new ConfigurationManager();
        this.threadLocalParsedClass = new ThreadLocal<>();
        this.threadLocalCurrentParsedClass = new ThreadLocal<>();
    }

    public Class<?> getParsedClass() {
        return threadLocalParsedClass.get();
    }

    public void setParsedClass(Class<?> parsedClass) {
        this.threadLocalParsedClass.set(parsedClass);
    }

    public Class<?> getCurrentParsedClass() {
        return threadLocalCurrentParsedClass.get();
    }

    public void setCurrentParsedClass(Class<?> currentParsedClass) {
        this.threadLocalCurrentParsedClass.set(currentParsedClass);
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
        threadLocalCurrentParsedClass.remove();
        threadLocalParsedClass.remove();
    }
}
