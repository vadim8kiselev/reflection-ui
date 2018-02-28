package com.kiselev.classparser.impl.reflection.parser.structure;

import com.kiselev.classparser.impl.reflection.parser.ClassNameParser;
import com.kiselev.classparser.impl.reflection.parser.base.AnnotationParser;
import com.kiselev.classparser.impl.reflection.parser.base.GenericTypeParser;
import com.kiselev.classparser.impl.reflection.parser.base.IndentParser;
import com.kiselev.classparser.impl.reflection.parser.base.ModifierParser;
import com.kiselev.classparser.impl.reflection.parser.base.ValueParser;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldParser {

    private AnnotationParser annotationParser = new AnnotationParser();

    private IndentParser indentParser = new IndentParser();

    private GenericTypeParser genericTypeParser = new GenericTypeParser();

    private ClassNameParser classNameParser = new ClassNameParser();

    private ValueParser valueParser = new ValueParser();

    private ModifierParser modifierParser = new ModifierParser();

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

        String annotations = annotationParser.getAnnotations(field);

        String indent = indentParser.getIndent(field);

        String modifiers = modifierParser.getModifiers(field.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type fieldType = isShowGeneric ? field.getGenericType() : field.getType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? field.getAnnotatedType() : null;

        String type = genericTypeParser.resolveType(fieldType, annotatedType);

        String fieldName = classNameParser.getMemberName(field);

        boolean isDisplayFiledValue = StateManager.getConfiguration().isDisplayFieldValue();

        String fieldValue = isDisplayFiledValue ? valueParser.getValue(field) : "";

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName + fieldValue + ";";

        return fieldSignature;
    }
}
