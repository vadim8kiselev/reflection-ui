package com.classparser.reflection.impl.parser.structure;

import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.base.*;
import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldParser {

    public static String getFields(Class<?> clazz) {
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

    private static String getField(Field field) {
        String fieldSignature = "";

        String annotations = AnnotationParser.getAnnotations(field);

        String indent = IndentParser.getIndent(field);

        String modifiers = ModifierParser.getModifiers(field.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type fieldType = isShowGeneric ? field.getGenericType() : field.getType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? field.getAnnotatedType() : null;

        String type = GenericTypeParser.resolveType(fieldType, annotatedType);

        String fieldName = ClassNameParser.getMemberName(field);

        boolean isDisplayFiledValue = StateManager.getConfiguration().isDisplayFieldValue();

        String fieldValue = isDisplayFiledValue ? ValueParser.getValue(field) : "";

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName + fieldValue + ";";

        return fieldSignature;
    }
}
