package com.kiselev.reflection.ui;

import com.kiselev.reflection.ui.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.classes.ClassUtils;
import com.kiselev.reflection.ui.constructor.ConstructorUtils;
import com.kiselev.reflection.ui.field.FieldUtils;
import com.kiselev.reflection.ui.generic.GenericsUtils;
import com.kiselev.reflection.ui.indent.IndentUtils;
import com.kiselev.reflection.ui.inheritance.InheritancesUtils;
import com.kiselev.reflection.ui.method.MethodUtils;
import com.kiselev.reflection.ui.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.packages.PackageUtils;
import com.kiselev.reflection.ui.type.TypeUtils;
import com.kiselev.reflection.ui.name.NameUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUIImpl implements ReflectionUI {

    public String parseClass(Class<?> clazz) {
        String parsedClass = "";

        String indent = new IndentUtils().getIndent(clazz);

        String classSignature = getClassSignature(clazz);

        String classContent = getClassContent(clazz);

        parsedClass += classSignature + "{\n\n" + classContent + indent + "}";

        return parsedClass;
    }

    private String getClassSignature(Class<?> clazz) {
        String classSignature = "";

        String packageName = new PackageUtils().getPackage(clazz);

        String annotations = new AnnotationUtils().getAnnotations(clazz);

        String indent = new IndentUtils().getIndent(clazz);

        String modifiers = new ModifiersUtils().getModifiers(clazz.getModifiers());

        String type = new TypeUtils().getType(clazz);

        String typeName = new NameUtils().getTypeName(clazz);

        String generics = new GenericsUtils().getGenerics(clazz);

        String inheritances = new InheritancesUtils().getInheritances(clazz);

        classSignature += packageName + annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = new FieldUtils().getFields(clazz);

        String constructors = new ConstructorUtils().getConstructors(clazz);

        String methods = new MethodUtils().getMethods(clazz);

        String classes = new ClassUtils().getClasses(clazz);

        classContent += composeContent(Arrays.asList(fields, constructors, methods, classes)); //fields + constructors + methods + classes;

        return classContent;
    }

    private String composeContent(List<String> contents) {
        List<String> nonEmptyContents = new ArrayList<>();

        for (String content : contents) {
            if (!content.isEmpty()) {
                nonEmptyContents.add(content);
            }
        }

        return String.join("\n", nonEmptyContents);
    }
}
