package com.kiselev.classparser.impl.reflection.classes;

import com.kiselev.classparser.api.ClassParser;
import com.kiselev.classparser.impl.reflection.ReflectionParser;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    private ClassParser classParser = new ReflectionParser();

    public String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(classParser.parseClass(declaredClass));
        }
        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!classList.isEmpty()) {
            classes += String.join(lineSeparator + lineSeparator, classList) + lineSeparator;
        }

        return classes;
    }
}
