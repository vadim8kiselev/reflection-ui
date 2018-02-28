package com.classparser.api;

import java.util.Map;

public interface ClassParser {

    /**
     * Get meta information of class
     *
     * @param clazz - class for which getting meta-info
     */
    String parseClass(Class<?> clazz);

    /**
     * Set configuration for displaying meta-info
     *
     * @param configuration - map with configuration
     */
    void setConfiguration(Map<String, Object> configuration);
}
