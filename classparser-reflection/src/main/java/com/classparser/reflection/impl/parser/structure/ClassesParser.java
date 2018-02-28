package com.classparser.reflection.impl.parser.structure;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.ReflectionParser;
import com.classparser.reflection.impl.state.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ClassesParser {

    private static final ClassParser CLASS_PARSER = new ReflectionParser();

    public static String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(CLASS_PARSER.parseClass(declaredClass));
        }
        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!classList.isEmpty()) {
            classes += String.join(lineSeparator + lineSeparator, classList) + lineSeparator;
        }

        return classes;
    }
}
