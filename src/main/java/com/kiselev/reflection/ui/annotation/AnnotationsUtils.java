package com.kiselev.reflection.ui.annotation;

import com.kiselev.reflection.ui.indent.IndentUtils;
import sun.reflect.annotation.AnnotationType;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationsUtils {

    public String getAnnotations(AnnotatedElement annotatedElement) {
        String annotations = "";

        String indent = new IndentUtils().getIndent(annotatedElement);

        for (Annotation annotation : annotatedElement.getAnnotations()) {
            annotations += indent + getAnnotation(annotation) + "\n";
        }

        return annotations;
    }

    private String getAnnotation(Annotation annotation) {
        String annotationSignature = "";

        String annotationSign = "@";

        String annotationName = annotation.annotationType().getSimpleName();

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
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            Class<? extends Annotation> annotationTypeClass = annotation.annotationType();
            Method[] methods = annotationTypeClass.getDeclaredMethods();

            for (Method method : methods) {
                method.setAccessible(true);
                map.put(method.getName(), method.invoke(annotation));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return map;
    }
}
