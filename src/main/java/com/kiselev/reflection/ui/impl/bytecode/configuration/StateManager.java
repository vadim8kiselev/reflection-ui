package com.kiselev.reflection.ui.impl.bytecode.configuration;

import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/17/2017.
 */
public class StateManager {

    private static ThreadLocal<ConfigurationManager> configurationLocalMap = new ThreadLocal<>();

    public static void registerConfiguration(Map<String, Object> configuration) {
        ConfigurationManager manager = configurationLocalMap.get();
        if (manager == null) {
            configurationLocalMap.set(new ConfigurationManager(configuration));
        }
        configurationLocalMap.get().isEnableClassFileByteCodeCollector();
    }

    public static ConfigurationManager getConfiguration() {
        ConfigurationManager manager = configurationLocalMap.get();
        if (manager == null) {
            configurationLocalMap.set(new ConfigurationManager());
        }

        return configurationLocalMap.get();
    }
}
