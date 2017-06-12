package com.kiselev.reflection.ui.api;

public interface ReflectionUI {

    String parseClass(Class<?> clazz);

    String parseByteCode(Class<?> clazz);
}
