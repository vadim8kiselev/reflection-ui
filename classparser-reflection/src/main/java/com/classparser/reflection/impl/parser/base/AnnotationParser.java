package com.classparser.reflection.impl.parser.base;

import com.classparser.exception.ReflectionParserException;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationParser {

    private final IndentParser indentParser;

    private final ReflectionParserManager manager;

    private GenericTypeParser genericTypeParser;

    private ValueParser valueParser;

    public AnnotationParser(IndentParser indentParser, ReflectionParserManager manager) {
        this.indentParser = indentParser;
        this.manager = manager;
    }

    private static Method retrieveValueMethodFromAnnotation(Class<? extends Annotation> annotationType) {
        for (Method method : annotationType.getDeclaredMethods()) {
            if ("value".equals(method.getName())) {
                return method;
            }
        }

        return null;
    }

    public String getInlineAnnotations(AnnotatedElement annotatedElement) {
        List<String> annotations = new ArrayList<>();

        if (annotatedElement != null) {
            String indent = indentParser.getIndent(annotatedElement);

            for (Annotation annotation : unrollAnnotations(annotatedElement.getDeclaredAnnotations())) {
                annotations.add(getAnnotation(annotation));
            }

            return indent + String.join(" ", annotations);
        }

        return "";
    }

    public String getAnnotations(AnnotatedElement annotatedElement) {
        StringBuilder annotations = new StringBuilder();

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (annotatedElement != null) {
            String indent = indentParser.getIndent(annotatedElement);

            for (Annotation annotation : unrollAnnotations(annotatedElement.getDeclaredAnnotations())) {
                annotations.append(indent).append(getAnnotation(annotation)).append(lineSeparator);
            }
        }

        return annotations.toString();
    }

    public String getAnnotation(Annotation annotation) {
        String annotationSignature = "";

        Character annotationSign = '@';

        String annotationName = genericTypeParser.resolveType(annotation.annotationType());

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
            annotationArguments += '(' + String.join(", ", arguments) + ')';
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
                    map.put(method.getName(), valueParser.getValue(value));
                }
            }
        } catch (Exception exception) {
            String annotationName = annotation.annotationType().getName();
            String message = MessageFormat.format("Can't get default annotation value for annotation: {0}",
                    annotationName);
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

        return annotations.toArray(new Annotation[0]);
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
            String message = MessageFormat.format("Can't get default annotation value for annotation: {0}",
                    annotationName);
            throw new ReflectionParserException(message, exception);
        }

        return annotations;
    }

    private boolean isDefaultValue(Object value, Object defaultValue) {
        if (manager.getConfigurationManager().isShowDefaultValueInAnnotation()) {
            return false;
        }

        if (!value.getClass().isArray()) {
            return value.equals(defaultValue);
        } else {
            Object[] arrayValue = valueParser.getArrayValues(value);
            Object[] arrayDefaultValue = valueParser.getArrayValues(defaultValue);
            return Arrays.deepEquals(arrayValue, arrayDefaultValue);
        }
    }

    public void setGenericTypeParser(GenericTypeParser genericTypeParser) {
        this.genericTypeParser = genericTypeParser;
    }

    public void setValueParser(ValueParser valueParser) {
        this.valueParser = valueParser;
    }
}
