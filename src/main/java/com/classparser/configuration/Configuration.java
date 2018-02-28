package com.classparser.configuration;

import java.util.Map;

public interface Configuration {

    /**
     * Get configuration in type map
     * <p>
     * key - String
     * value - Object
     */
    Map<String, Object> getConfiguration();
}
