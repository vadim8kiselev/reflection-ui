package com.kiselev.classparser.impl.reflection.annotation;

import com.kiselev.classparser.exception.ReflectionParserException;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.indent.IndentUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;
import com.kiselev.classparser.impl.reflection.value.ValueUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationUtils {

    private IndentUtils indentUtils = new IndentUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private ValueUtils valueUtils = new ValueUtils();

    public String getInlineAnnotations(AnnotatedElement annotatedElement) {
        List<String> annotations = new ArrayList<>();

        if (annotatedElement != null) {
            String indent = indentUtils.getIndent(annotatedElement);

            for (Annotation annotation : unrollAnnotations(annotatedElement.getDeclaredAnnotations())) {
                annotations.add(getAnnotation(annotation));
            }

            return indent + String.join(" ", annotations);
        }

        return "";
    }

    public String getAnnotations(AnnotatedElement annotatedElement) {
        StringBuilder annotations = new StringBuilder();

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (annotatedElement != null) {
            String indent = indentUtils.getIndent(annotatedElement);

            for (Annotation annotation : unrollAnnotations(annotatedElement.getDeclaredAnnotations())) {
                annotations.append(indent).append(getAnnotation(annotation)).append(lineSeparator);
            }
        }

        return annotations.toString();
    }

    public String getAnnotation(Annotation annotation) {
        String annotationSignature = "";

        String annotationSign = "@";

        String annotationName = genericsUtils.resolveType(annotation.annotationType());

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
                if (!isDefaultValue(value, method.getDefaultValue())) {
                    map.put(method.getName(), valueUtils.getValue(value));
                }
            }
        } catch (Exception exception) {
            String annotationName = annotation.annotationType().getName();
            String message = String.format("Can't get default annotation value for annotation: %s", annotationName);
            throw new ReflectionParserException(message, exception);
        }

        return map;
    }

    private Annotation[] unrollAnnotations(Annotation[] declaredAnnotations) {
        List<Annotation> annotations = new ArrayList<>();

        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (isRepeatableAnnotation(declaredAnnotation)) {
                annotations.addAll(retrieveRepeatableAnnotations(declaredAnnotation));
            } else {
                annotations.add(declaredAnnotation);
            }
        }

        return annotations.toArray(new Annotation[annotations.size()]);
    }

    private boolean isRepeatableAnnotation(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Method valueMethod = retrieveValueMethodFromAnnotation(annotationType);
        if (valueMethod != null) {
            Class<?> returnType = valueMethod.getReturnType();

            if (returnType.isArray()) {
                Class<?> componentType = returnType.getComponentType();
                Repeatable repeatable = componentType.getAnnotation(Repeatable.class);
                return repeatable != null && annotationType.equals(repeatable.value());
            }
        }
        return false;
    }

    private Method retrieveValueMethodFromAnnotation(Class<? extends Annotation> annotationType) {
        for (Method method : annotationType.getDeclaredMethods()) {
            if ("value".equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    private List<Annotation> retrieveRepeatableAnnotations(Annotation annotation) {
        List<Annotation> annotations = new ArrayList<>();

        try {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            Method valueMethod = retrieveValueMethodFromAnnotation(annotationType);
            if (valueMethod != null) {
                valueMethod.setAccessible(true);
                Annotation[] retrievedAnnotations = (Annotation[]) valueMethod.invoke(annotation);
                annotations.addAll(Arrays.asList(retrievedAnnotations));
            }
        } catch (ReflectiveOperationException exception) {
            String annotationName = annotation.annotationType().getName();
            String message = String.format("Can't get default annotation value for annotation: %s", annotationName);
            throw new ReflectionParserException(message, exception);
        }

        return annotations;
    }

    private boolean isDefaultValue(Object value, Object defaultValue) {
        if (StateManager.getConfiguration().isShowDefaultValueInAnnotation()) {
            return false;
        }

        if (!value.getClass().isArray()) {
            return value.equals(defaultValue);
        } else {
            Object[] arrayValue = valueUtils.getArrayValues(value);
            Object[] arrayDefaultValue = valueUtils.getArrayValues(defaultValue);
            return Arrays.equals(arrayValue, arrayDefaultValue);
        }
    }
}
