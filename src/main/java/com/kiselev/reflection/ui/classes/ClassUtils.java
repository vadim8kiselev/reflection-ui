package com.kiselev.reflection.ui.classes;

import com.kiselev.reflection.ui.ReflectionUIImpl;

import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

    public String getClasses(Class<?> clazz) {
        String classes = "";

        List<String> classList = new ArrayList<String>();
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            classList.add(new ReflectionUIImpl().parseClass(declaredClass));
        }

        if (!classList.isEmpty()) {
            classes += String.join("\n\n", classList) + "\n";
        }

        return classes;
    }
}
