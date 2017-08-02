package com.kiselev.reflection.ui.impl.reflection.value;

import com.kiselev.reflection.ui.exception.ReflectionParserException;
import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static com.kiselev.reflection.ui.impl.reflection.constants.CastConstants.FIELD;
import static com.kiselev.reflection.ui.impl.reflection.constants.CastConstants.METHOD;
import static com.kiselev.reflection.ui.impl.reflection.constants.CastConstants.ANNOTATION;
import static com.kiselev.reflection.ui.impl.reflection.constants.CastConstants.CLASS;

public class ValueUtils {

    public String getValue(Object object) {
        if (object instanceof Field && FIELD.cast(object).getDeclaringClass() == StateManager.getCurrentClass()) {
            return getFieldValue(FIELD.cast(object));
        }

        if (object instanceof Method && METHOD.cast(object).getDeclaringClass() == StateManager.getCurrentClass()) {
            return getDefaultAnnotationValue(METHOD.cast(object));
        }

        if (object != null) {
            Class<?> clazz = object.getClass();
            if (clazz.isArray()) {
                List<String> listValues = new ArrayList<>();
                for (Object listValue : getArrayValues(object)) {
                    if (!isObjectValue(listValue)) {
                        listValues.add(getValue(listValue));
                    }
                }

                String values = String.join(", ", listValues);
                if (listValues.size() == 1 || values.isEmpty()) {
                    return values;
                } else {
                    return "{" + values + "}";
                }
            }

            if (clazz.isEnum()) return new GenericsUtils().resolveType(clazz) + "." + object;
            if (object instanceof String) return "\"" + object + "\"";
            if (object instanceof Character) return "\'" + object + "\'";
            if (object instanceof Number || object instanceof Boolean) return object.toString() + getLiteral(object);
            if (object instanceof Annotation) return new AnnotationUtils().getAnnotation(ANNOTATION.cast(object));
            if (object instanceof Class) return new GenericsUtils().resolveType(CLASS.cast(object)) + ".class";
            return "";
        }

        return null;
    }

    private boolean isObjectValue(Object object) {
        return object != null && !(object instanceof String) && object.toString().isEmpty();
    }

    private List<Object> getArrayValues(Object object) {
        List<Object> objects = new ArrayList<>();
        if (object.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(object); i++) {
                objects.add(Array.get(object, i));
            }
        }
        return objects;
    }

    private String getLiteral(Object object) {
        if (object instanceof Long) return "L";
        if (object instanceof Float) return "f";
        if (object instanceof Double) return "d";
        return "";
    }

    private String getDefaultAnnotationValue(Method method) {
        String defaultAnnotationValue = "";

        if (method.getDeclaringClass().isAnnotation()) {
            String defaultValue = new ValueUtils().getValue(method.getDefaultValue());

            if (defaultValue != null) {
                defaultAnnotationValue += " default " + defaultValue;
            }
        }

        return defaultAnnotationValue;
    }

    private String getFieldValue(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                field.setAccessible(true);
                String fieldValue = new ValueUtils().getValue(field.get(null));
                if (!"".equals(fieldValue)) {
                    return " = " + fieldValue;
                }
            } catch (IllegalAccessException exception) {
                throw new ReflectionParserException("Can't get value of field: " + field.getName(), exception);
            }
        }

        return "";
    }
}
