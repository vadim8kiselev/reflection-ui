package com.kiselev.classparser.impl.reflection.value;

import com.kiselev.classparser.exception.ReflectionParserException;
import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.constants.Cast;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ValueUtils {

    public String getValue(Object object) {
        if (isField(object)) {
            return getFieldValue(Cast.FIELD.cast(object));
        }

        if (isAnnotationMethod(object)) {
            return getDefaultAnnotationValue(Cast.METHOD.cast(object));
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

            if (clazz.isEnum()) {
                return new GenericsUtils().resolveType(clazz) + "." + object;
            } else if (object instanceof String) {
                return "\"" + object + "\"";
            } else if (object instanceof Character) {
                return "\'" + object + "\'";
            } else if (object instanceof Number || object instanceof Boolean) {
                return object.toString() + getLiteral(object);
            } else if (object instanceof Annotation) {
                return new AnnotationUtils().getAnnotation(Cast.ANNOTATION.cast(object));
            } else if (object instanceof Class) {
                return new GenericsUtils().resolveType(Cast.CLASS.cast(object)) + ".class";
            } else {
                return "";
            }
        }

        return null;
    }

    private boolean isObjectValue(Object object) {
        return object != null && !(object instanceof String) && object.toString().isEmpty();
    }

    public Object[] getArrayValues(Object object) {
        Object[] objects = new Object[0];
        if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            object = new Object[length];

            for (int i = 0; i < Array.getLength(object); i++) {
                objects[i] = Array.get(object, i);
            }
        }

        return objects;
    }

    private String getLiteral(Object object) {
        if (object instanceof Long) {
            return "L";
        } else if (object instanceof Float) {
            return "f";
        } else if (object instanceof Double) {
            return "d";
        } else {
            return "";
        }
    }

    private String getDefaultAnnotationValue(Method method) {
        String defaultAnnotationValue = "";

        String defaultValue = getValue(method.getDefaultValue());

        if (defaultValue != null) {
            defaultAnnotationValue += " default " + defaultValue;
        }

        return defaultAnnotationValue;
    }

    private String getFieldValue(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                field.setAccessible(true);
                Object value = field.get(null);
                if (!isField(value)) {
                    String fieldValue = getValue(value);
                    if (!"".equals(fieldValue)) {
                        return " = " + fieldValue;
                    }
                }
            } catch (IllegalAccessException exception) {
                throw new ReflectionParserException("Can't get value of field: " + field.getName(), exception);
            }
        }

        return "";
    }

    private boolean isField(Object object) {
        return object instanceof Field &&
                Cast.FIELD.cast(object).getDeclaringClass() == StateManager.getCurrentClass();
    }

    private boolean isAnnotationMethod(Object object) {
        return object instanceof Method &&
                Cast.METHOD.cast(object).getDeclaringClass().isAnnotation();
    }
}
