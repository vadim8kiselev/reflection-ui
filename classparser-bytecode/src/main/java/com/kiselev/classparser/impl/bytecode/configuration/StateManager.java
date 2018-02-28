package com.kiselev.classparser.impl.bytecode.configuration;

import java.util.Map;

public class StateManager {

    private static ThreadLocal<ConfigurationManager> configurationLocalMap = new ThreadLocal<>();

    public static void registerConfiguration(Map<String, Object> configuration) {
        ConfigurationManager manager = configurationLocalMap.get();
        if (manager == null) {
            configurationLocalMap.set(new ConfigurationManager(configuration));
        }
    }

    public static ConfigurationManager getConfiguration() {
        ConfigurationManager manager = configurationLocalMap.get();
        if (manager == null) {
            configurationLocalMap.set(new ConfigurationManager());
        }

        return configurationLocalMap.get();
    }
}
