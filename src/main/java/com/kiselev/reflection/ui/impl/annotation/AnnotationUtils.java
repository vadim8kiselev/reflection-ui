package com.kiselev.reflection.ui.impl.annotation;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.value.ValueUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationUtils {

    public String getInlineAnnotations(AnnotatedElement annotatedElement) {
        return getAnnotations(annotatedElement, null);
    }

    public String getInlineAnnotations(AnnotatedElement annotatedElement, Class<?> parsedClass) {
        String annotation = getAnnotations(annotatedElement, parsedClass);
        if (!"".equals(annotation)) {
            annotation = annotation.substring(0, annotation.length() - 1) + " ";
        }

        return annotation;
    }

    public String getAnnotations(AnnotatedElement annotatedElement) {
        return getAnnotations(annotatedElement, null);
    }

    public String getAnnotations(AnnotatedElement annotatedElement, Class<?> parsedClass) {
        StringBuilder annotations = new StringBuilder();

        String indent = new IndentUtils().getIndent(annotatedElement);

        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
            annotations.append(indent).append(getAnnotation(annotation, parsedClass)).append("\n");
        }

        return annotations.toString();
    }

    public String getAnnotation(Annotation annotation) {
        return getAnnotation(annotation, null);
    }

    public String getAnnotation(Annotation annotation, Class<?> parsedClass) {
        String annotationSignature = "";

        String annotationSign = "@";

        String annotationName = new GenericsUtils().resolveType(annotation.annotationType(), parsedClass);

        String annotationArguments = getAnnotationArguments(annotation, parsedClass);

        annotationSignature += annotationSign + annotationName + annotationArguments;

        return annotationSignature;
    }

    private String getAnnotationArguments(Annotation annotation, Class<?> parsedClass) {
        String annotationArguments = "";

        List<String> arguments = new ArrayList<>();

        for (Map.Entry<String, Object> entry : getAnnotationMemberTypes(annotation, parsedClass).entrySet()) {
            arguments.add(entry.getKey() + " = " + entry.getValue());
        }

        if (!arguments.isEmpty()) {
            annotationArguments += "(" + String.join(", ", arguments) + ")";
        }

        return annotationArguments;
    }

    private Map<String, Object> getAnnotationMemberTypes(Annotation annotation, Class<?> parsedClass) {
        Map<String, Object> map = new HashMap<>();

        try {
            Class<? extends Annotation> annotationTypeClass = annotation.annotationType();
            Method[] methods = annotationTypeClass.getDeclaredMethods();

            for (Method method : methods) {
                method.setAccessible(true);
                Object value = method.invoke(annotation);
                map.put(method.getName(), new ValueUtils().getValue(value, parsedClass));
            }
        } catch (Exception exception) {
            // Sin
        }

        return map;
    }
}
