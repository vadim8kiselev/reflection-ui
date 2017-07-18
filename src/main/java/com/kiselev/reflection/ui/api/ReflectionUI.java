package com.kiselev.reflection.ui.api;

import java.util.Map;

public interface ReflectionUI {

    /**
     * Get meta information of class
     *
     * @param clazz - class for which getting meta-info
     *
     * */
    String parseClass(Class<?> clazz);

    /**
     * Set configuration for displaying meta-info
     *
     * @param configuration - map with configuration
     *
     * */
    void setConfiguration(Map<String, Object> configuration);
}
