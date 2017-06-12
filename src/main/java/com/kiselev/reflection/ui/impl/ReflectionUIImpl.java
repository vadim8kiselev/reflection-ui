package com.kiselev.reflection.ui.impl;

import com.kiselev.reflection.ui.bytecode.holder.ByteCodeHolder;
import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.impl.classes.ClassUtils;
import com.kiselev.reflection.ui.impl.constructor.ConstructorUtils;
import com.kiselev.reflection.ui.impl.field.FieldUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.inheritance.InheritancesUtils;
import com.kiselev.reflection.ui.impl.method.MethodUtils;
import com.kiselev.reflection.ui.impl.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.packages.PackageUtils;
import com.kiselev.reflection.ui.impl.type.TypeUtils;
import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.lang.instrument.Instrumentation;
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

    @Override
    public String parseByteCode(Class<?> clazz) {
        String decompilledByteCode = ByteCodeHolder.getDecompilledByteCode(clazz);

        return decompilledByteCode;
    }
}
