package com.classparser.reflection.impl.parser.structure;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.ReflectionParser;
import com.classparser.reflection.impl.state.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ClassesParser {

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
