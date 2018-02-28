package com.classparser.reflection.impl.state;

import com.classparser.reflection.impl.parser.imports.ImportParser;
import com.classparser.reflection.impl.configuration.ConfigurationManager;

public class State {

    private ImportParser importParser;

    private Class<?> mainParsedClass;

    private Class<?> currentParsedClass;

    private ConfigurationManager configurationManager;

    public State(ImportParser importParser, Class<?> mainParsedClass, Class<?> currentParsedClass) {
        this.importParser = importParser;
        this.mainParsedClass = mainParsedClass;
        this.currentParsedClass = currentParsedClass;
    }

    public ImportParser getImportParser() {
        return importParser;
    }

    public Class<?> getMainParsedClass() {
        return mainParsedClass;
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

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void clearState() {
        mainParsedClass = null;
        currentParsedClass = null;
        importParser = null;
    }
}