package com.kiselev.reflection.ui.impl.field;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.value.ValueUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class FieldUtils {

    public String getFields(Class<?> clazz) {
        String fields = "";

        List<String> fieldList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldList.add(getField(field));
        }
        if (!fieldList.isEmpty()) {
            fields += String.join("\n\n", fieldList) + "\n";
        }

        return fields;
    }

    private String getField(Field field) {
        String fieldSignature = "";

        String annotations = new AnnotationUtils().getAnnotations(field);

        String indent = new IndentUtils().getIndent(field);

        String modifiers = new ModifiersUtils().getModifiers(field.getModifiers());

        String type = new GenericsUtils().resolveType(field.getGenericType(), field.getAnnotatedType());

        String fieldName = field.getName();

        String fieldValue = getFieldValue(field);

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName
                + ("".equals(fieldValue) ? "" : " = " + fieldValue) + ";";

        return fieldSignature;
    }

    private String getFieldValue(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                field.setAccessible(true);
                return new ValueUtils().getValue(field.get(null));
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }

        return "";
    }
}
