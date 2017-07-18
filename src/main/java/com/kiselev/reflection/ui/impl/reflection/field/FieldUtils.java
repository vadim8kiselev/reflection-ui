package com.kiselev.reflection.ui.impl.reflection.field;

import com.kiselev.reflection.ui.exception.ReflectionParserException;
import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;
import com.kiselev.reflection.ui.impl.reflection.value.ValueUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldUtils {

    public String getFields(Class<?> clazz) {
        String fields = "";

        List<String> fieldList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldList.add(getField(field));
        }

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!fieldList.isEmpty()) {
            fields += String.join(lineSeparator + lineSeparator, fieldList) + lineSeparator;
        }

        return fields;
    }

    private String getField(Field field) {
        String fieldSignature = "";

        String annotations = new AnnotationUtils().getAnnotations(field);

        String indent = new IndentUtils().getIndent(field);

        String modifiers = new ModifiersUtils().getModifiers(field.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type fieldType = isShowGeneric ? field.getGenericType() : field.getType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? field.getAnnotatedType() : null;

        String type = new GenericsUtils().resolveType(fieldType, annotatedType);

        String fieldName = new NameUtils().getMemberName(field);

        String fieldValue = StateManager.getConfiguration().isDisplayFieldValue() ? getFieldValue(field) : "";

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName + fieldValue + ";";

        return fieldSignature;
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
