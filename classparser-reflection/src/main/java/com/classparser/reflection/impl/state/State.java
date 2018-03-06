package com.classparser.reflection.impl.state;

import com.classparser.reflection.impl.configuration.ConfigurationManager;
import com.classparser.reflection.impl.parser.imports.ImportParser;

class State {

    private ImportParser importParser;

    private Class<?> mainParsedClass;

    private Class<?> currentParsedClass;

    private ConfigurationManager configurationManager;

    State() {
    }

    State(ImportParser importParser, Class<?> mainParsedClass, Class<?> currentParsedClass) {
        this.importParser = importParser;
        this.mainParsedClass = mainParsedClass;
        this.currentParsedClass = currentParsedClass;
    }

    ImportParser getImportParser() {
        return importParser;
    }

    Class<?> getMainParsedClass() {
        return mainParsedClass;
    }

    Class<?> getCurrentParsedClass() {
        return currentParsedClass;
    }

    void setCurrentParsedClass(Class<?> currentParsedClass) {
        this.currentParsedClass = currentParsedClass;
    }

    ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    void clearState() {
        mainParsedClass = null;
        currentParsedClass = null;
        importParser = null;
    }
}
