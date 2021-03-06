package com.kiselev.reflection.ui.impl.classes;

import com.kiselev.reflection.ui.impl.ReflectionUIImpl;

import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    public String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(new ReflectionUIImpl().parseClass(declaredClass));
        }

        if (!classList.isEmpty()) {
            classes += String.join("\n\n", classList) + "\n";
        }

        return classes;
    }
}
