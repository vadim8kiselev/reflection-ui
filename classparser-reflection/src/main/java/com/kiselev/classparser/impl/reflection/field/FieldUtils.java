package com.kiselev.classparser.impl.reflection.field;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.argument.ArgumentUtils;
import com.kiselev.classparser.impl.reflection.exception.ExceptionUtils;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.indent.IndentUtils;
import com.kiselev.classparser.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.classparser.impl.reflection.name.NameUtils;
import com.kiselev.classparser.impl.reflection.packages.PackageUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;
import com.kiselev.classparser.impl.reflection.value.ValueUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FieldUtils {

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private IndentUtils indentUtils = new IndentUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private NameUtils nameUtils = new NameUtils();

    private ValueUtils valueUtils = new ValueUtils();

    private ModifiersUtils modifiersUtils = new ModifiersUtils();

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

        String annotations = annotationUtils.getAnnotations(field);

        String indent = indentUtils.getIndent(field);

        String modifiers = modifiersUtils.getModifiers(field.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type fieldType = isShowGeneric ? field.getGenericType() : field.getType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? field.getAnnotatedType() : null;

        String type = genericsUtils.resolveType(fieldType, annotatedType);

        String fieldName = nameUtils.getMemberName(field);

        boolean isDisplayFiledValue = StateManager.getConfiguration().isDisplayFieldValue();

        String fieldValue = isDisplayFiledValue ? valueUtils.getValue(field) : "";

        fieldSignature += annotations + indent + modifiers + type + " " + fieldName + fieldValue + ";";

        return fieldSignature;
    }
}
