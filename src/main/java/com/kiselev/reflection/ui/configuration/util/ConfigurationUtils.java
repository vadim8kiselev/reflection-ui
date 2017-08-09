package com.kiselev.reflection.ui.configuration.util;

import com.kiselev.reflection.ui.exception.OptionNotFoundException;

import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/28/2017.
 */
public class ConfigurationUtils {

    private final Map<String, Object> defaultConfiguration;

    private Map<String, Object> configuration;

    public ConfigurationUtils(Map<String, Object> configuration, Map<String, Object> defaultConfiguration) {
        this.configuration = configuration;
        this.defaultConfiguration = defaultConfiguration;
    }

    private <T> boolean isInstance(Object object, Class<T> type) {
        return type.isInstance(object);
    }

    public <T> T getConfig(String config, Class<T> type) {
        Object option = configuration.get(config);
        if (isInstance(option, type)) {
            return type.cast(option);
        } else {
            if (!defaultConfiguration.containsKey(config)) {
                String exceptionMessage = String.format("Default option: \"%s\" it isn't put down", config);
                throw new OptionNotFoundException(exceptionMessage);
            }
            return type.cast(defaultConfiguration.get(config));
        }
    }

    public void appendConfiguration(Map<String, Object> configuration) {
        this.configuration.putAll(configuration);
    }
}
