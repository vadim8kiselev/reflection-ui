package com.kiselev.reflection.ui.impl.reflection.classes;

import com.kiselev.reflection.ui.impl.reflection.ReflectionParser;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    public String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(new ReflectionParser().parseClass(declaredClass));
        }
        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!classList.isEmpty()) {
            classes += String.join(lineSeparator + lineSeparator, classList) + lineSeparator;
        }

        return classes;
    }
}
