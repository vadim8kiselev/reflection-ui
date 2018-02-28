package com.kiselev.classparser.impl.reflection.state;

import com.kiselev.classparser.impl.reflection.configuration.ConfigurationManager;
import com.kiselev.classparser.impl.reflection.imports.ImportUtils;

/**
 * Created by Aleksei Makarov on 28.02.2018.
 */
public class State {

    private ImportUtils importUtils;

    private Class<?> mainParsedClass;

    private Class<?> currentParsedClass;

    private ConfigurationManager configurationManager;

    public State(ImportUtils importUtils, Class<?> mainParsedClass, Class<?> currentParsedClass) {
        this.importUtils = importUtils;
        this.mainParsedClass = mainParsedClass;
        this.currentParsedClass = currentParsedClass;
    }

    public ImportUtils getImportUtils() {
        return importUtils;
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
        importUtils = null;
    }
}
