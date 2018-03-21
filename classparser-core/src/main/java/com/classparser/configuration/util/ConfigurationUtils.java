package com.classparser.configuration.util;

import com.classparser.exception.option.OptionNotFoundException;

import java.text.MessageFormat;
import java.util.Map;

public class ConfigurationUtils {

    private final Map<String, Object> defaultConfiguration;

    private final Map<String, Object> configuration;

    public ConfigurationUtils(Map<String, Object> configuration, Map<String, Object> defaultConfiguration) {
        this.configuration = configuration;
        this.defaultConfiguration = defaultConfiguration;
    }

    private <T> boolean isInstance(Object object, Class<T> type) {
        return type.isInstance(object);
    }

    public void reloadConfiguration(Map<String, Object> newConfiguration) {
        this.configuration.putAll(newConfiguration);
    }

    public <T> T getConfig(String config, Class<T> type) {
        Object option = configuration.get(config);
        if (isInstance(option, type)) {
            return type.cast(option);
        } else {
            if (!defaultConfiguration.containsKey(config)) {
                String message = MessageFormat.format("Default option: \"{0}\" it isn't put down", config);
                throw new OptionNotFoundException(message);
            }
            return type.cast(defaultConfiguration.get(config));
        }
    }

    public void appendConfiguration(Map<String, Object> configuration) {
        this.configuration.putAll(configuration);
    }
}
