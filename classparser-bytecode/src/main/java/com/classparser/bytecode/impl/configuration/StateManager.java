package com.classparser.bytecode.impl.configuration;

import java.util.Map;

public class StateManager {

    private static final ThreadLocal<ConfigurationManager> configurationLocalMap = new ThreadLocal<>();

    public static void registerConfiguration(Map<String, Object> configuration) {
        if (configuration != null) {
            ConfigurationManager manager = configurationLocalMap.get();
            if (manager == null) {
                configurationLocalMap.set(new ConfigurationManager(configuration));
            } else {
                manager.reloadConfiguration(configuration);
            }
        }
    }

    public static ConfigurationManager getConfiguration() {
        ConfigurationManager manager = configurationLocalMap.get();
        if (manager == null) {
            manager = new ConfigurationManager();
            configurationLocalMap.set(manager);
        }

        return manager;
    }
}
