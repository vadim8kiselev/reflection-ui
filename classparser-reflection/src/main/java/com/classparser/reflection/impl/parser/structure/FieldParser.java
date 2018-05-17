package com.classparser.reflection.impl.parser.structure;

import com.classparser.reflection.impl.configuration.ConfigurationManager;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.base.ValueParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldParser {

    private final ReflectionParserManager manager;

    private final AnnotationParser annotationParser;

    private final IndentParser indentParser;

    private final ModifierParser modifierParser;

    private final GenericTypeParser genericTypeParser;

    private final ClassNameParser classNameParser;

    private final ValueParser valueParser;

    public FieldParser(ReflectionParserManager manager, AnnotationParser annotationParser,
                       IndentParser indentParser, ModifierParser modifierParser,
                       GenericTypeParser genericTypeParser, ClassNameParser classNameParser, ValueParser valueParser) {
        this.manager = manager;
        this.annotationParser = annotationParser;
        this.indentParser = indentParser;
        this.modifierParser = modifierParser;
        this.genericTypeParser = genericTypeParser;
        this.classNameParser = classNameParser;
        this.valueParser = valueParser;
    }

    public String getFields(Class<?> clazz) {
        String fields = "";

        List<String> fieldList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldList.add(getField(field));
        }

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (!fieldList.isEmpty()) {
            fields += String.join(lineSeparator + lineSeparator, fieldList) + lineSeparator;
        }

        return fields;
    }

    private String getField(Field field) {
        ConfigurationManager configurationManager = manager.getConfigurationManager();

        String fieldSignature = "";

        String annotations = annotationParser.getAnnotations(field);

        String indent = indentParser.getIndent(field);

        String modifiers = modifierParser.getModifiers(field.getModifiers());

        boolean isShowGeneric = configurationManager.isShowGenericSignatures();

        Type fieldType = isShowGeneric ? field.getGenericType() : field.getType();

        boolean isShowTypeAnnotation = configurationManager.isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? field.getAnnotatedType() : null;

        String type = genericTypeParser.resolveType(fieldType, annotatedType);

        String fieldName = classNameParser.getMemberName(field);

        boolean isDisplayFiledValue = configurationManager.isDisplayFieldValue();

        String fieldValue = isDisplayFiledValue ? valueParser.getValue(field) : "";

        fieldSignature += annotations + indent + modifiers + type + ' ' + fieldName + fieldValue + ';';

        return fieldSignature;
    }
}
