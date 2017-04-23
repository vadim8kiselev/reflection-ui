package com.kiselev.reflection.ui.annotation;

import com.kiselev.reflection.ui.indent.IndentUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
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

                Object value = method.invoke(annotation);
                String fullValue = value.toString();

                if (value.getClass().isArray()) {
                    List<String> listValue = new ArrayList<String>();
                    for (Object arrayValue : getArrayValues(value)) {
                        if (arrayValue.getClass().isEnum()) {
                            listValue.add(getEnumValue(arrayValue));
                        } else {
                            listValue.add(arrayValue.toString());
                        }
                    }

                    fullValue = "[" + String.join(", ", listValue) + "]";
                }

                if (value.getClass().isEnum()) {
                    fullValue = getEnumValue(value);
                }

                map.put(method.getName(), fullValue);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return map;
    }

    private String getEnumValue(Object object) {
        return object.getClass().getSimpleName() + "." + object;
    }

    private Object[] getArrayValues(Object object) {
        int length = Array.getLength(object);
        Object[] objects = new Object[length];

        for (int i = 0; i < length; i++) {
            objects[i] = (Array.get(object, i));
        }
        return objects;
    }
}
