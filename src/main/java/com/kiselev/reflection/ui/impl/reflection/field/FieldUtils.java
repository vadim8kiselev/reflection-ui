package com.kiselev.reflection.ui.impl.reflection.field;

import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.value.ValueUtils;

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

        String fieldName = new NameUtils().getMemberName(field);

        String fieldValue = getFieldValue(field);

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
                throw new RuntimeException(exception);
            } catch (NoClassDefFoundError | ExceptionInInitializerError error) {
                //TODO : Think up about this case
            }
        }

        return "";
    }
}
