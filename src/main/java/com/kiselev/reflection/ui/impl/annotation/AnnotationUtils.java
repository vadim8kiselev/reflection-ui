package com.kiselev.reflection.ui.impl.annotation;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.value.ValueUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationUtils {

    public String getInlineAnnotations(AnnotatedElement annotatedElement) {
        String annotation = getAnnotations(annotatedElement);
        if (!annotation.isEmpty()) {
            annotation = annotation.substring(0, annotation.length() - 1).replace("\n", " ");
        }

        return annotation;
    }

    public String getAnnotations(AnnotatedElement annotatedElement) {
        StringBuilder annotations = new StringBuilder();

        String indent = new IndentUtils().getIndent(annotatedElement);

        for (Annotation annotation : annotatedElement.getDeclaredAnnotations()) {
            String repeatable = getRepeatable(annotation, indent);
            if (!repeatable.isEmpty()) {
                annotations.append(repeatable);
            } else {
                annotations.append(indent).append(getAnnotation(annotation)).append("\n");
            }
        }

        return annotations.toString();
    }

    private String getAnnotations(List<Annotation> annotations, String indent) {
        StringBuilder builder = new StringBuilder();

        for (Annotation annotation : annotations) {
            builder.append(indent).append(getAnnotation(annotation)).append("\n");
        }

        return builder.toString();
    }

    public String getAnnotation(Annotation annotation) {
        String annotationSignature = "";

        String annotationSign = "@";

        String annotationName = new GenericsUtils().resolveType(annotation.annotationType());

        String annotationArguments = getAnnotationArguments(annotation);

        annotationSignature += annotationSign + annotationName + annotationArguments;

        return annotationSignature;
    }

    private String getAnnotationArguments(Annotation annotation) {
        String annotationArguments = "";

        List<String> arguments = new ArrayList<>();

        for (Map.Entry<String, Object> entry : getAnnotationMemberTypes(annotation).entrySet()) {
            arguments.add(entry.getKey() + " = " + entry.getValue());
        }

        if (!arguments.isEmpty()) {
            annotationArguments += "(" + String.join(", ", arguments) + ")";
        }

        return annotationArguments;
    }

    private Map<String, Object> getAnnotationMemberTypes(Annotation annotation) {
        Map<String, Object> map = new HashMap<>();

        try {
            Class<? extends Annotation> annotationTypeClass = annotation.annotationType();
            Method[] methods = annotationTypeClass.getDeclaredMethods();

            for (Method method : methods) {
                method.setAccessible(true);
                Object value = method.invoke(annotation);
                map.put(method.getName(), new ValueUtils().getValue(value));
            }
        } catch (Exception exception) {
            // Sin
        }

        return map;
    }

    private String getRepeatable(Annotation annotation, String indent) {
        String repeatableAnnotations = "";
        Class<? extends Annotation> annotationTypeClass = annotation.annotationType();
        try {
            Method value = annotationTypeClass.getMethod("value");
            Class<?> type = value.getReturnType();
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (componentType.isAnnotation() && componentType.isAnnotationPresent(Repeatable.class)) {
                    Object invoke = value.invoke(annotation);
                    List<Annotation> annotations = new ArrayList<>();

                    for (int i = 0; i < Array.getLength(invoke); i++) {
                        annotations.add(Annotation.class.cast(Array.get(invoke, i)));
                    }

                    repeatableAnnotations = getAnnotations(annotations, indent);
                }
            }
        } catch (Exception exception) {
            //Sin
        }

        return repeatableAnnotations;
    }
}
