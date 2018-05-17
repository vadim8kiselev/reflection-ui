package com.classparser.reflection.impl.parser.structure;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.util.ArrayList;
import java.util.List;

public class ClassesParser {

    private final ClassParser classParser;

    private final ReflectionParserManager manager;

    public ClassesParser(ClassParser classParser, ReflectionParserManager manager) {
        this.classParser = classParser;
        this.manager = manager;
    }

    public String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(classParser.parseClass(declaredClass));
        }
        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (!classList.isEmpty()) {
            classes += String.join(lineSeparator + lineSeparator, classList) + lineSeparator;
        }

        return classes;
    }
}
