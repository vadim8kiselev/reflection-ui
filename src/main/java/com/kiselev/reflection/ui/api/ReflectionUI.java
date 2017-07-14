package com.kiselev.reflection.ui.api;

import java.util.Map;

public interface ReflectionUI {

    String parseClass(Class<?> clazz);

    void setConfiguration(Map<String, Object> configuration);
}
