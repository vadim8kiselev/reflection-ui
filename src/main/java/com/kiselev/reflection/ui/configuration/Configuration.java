package com.kiselev.reflection.ui.configuration;

import java.util.Map;

/**
 * Created by Aleksei Makarov on 06/24/2017.
 */
public interface Configuration {

    /**
     * Get configuration in type map
     * <p>
     * key - String
     * value - Object
     */
    Map<String, Object> getConfiguration();
}
