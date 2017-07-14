package com.kiselev.reflection.ui.api;

import com.kiselev.reflection.ui.configuration.Configuration;

public interface ReflectionUI {

    String parseClass(Class<?> clazz);

    void setConfiguration(Configuration configuration);
}
