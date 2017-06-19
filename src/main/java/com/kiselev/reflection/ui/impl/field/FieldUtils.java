package com.kiselev.reflection.ui.impl.field;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.modifier.ModifiersUtils;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName + ";";

        return fieldSignature;
    }
}
