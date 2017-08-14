package com.kiselev.classparser.impl.reflection.constructor;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.argument.ArgumentUtils;
import com.kiselev.classparser.impl.reflection.exception.ExceptionUtils;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.indent.IndentUtils;
import com.kiselev.classparser.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorUtils {

    public String getConstructors(Class<?> clazz) {
        String constructors = "";

        List<String> constructorList = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructorList.add(getConstructor(constructor));
        }

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!constructorList.isEmpty()) {
            constructors += String.join(lineSeparator + lineSeparator, constructorList) + lineSeparator;
        }

        return constructors;
    }

    private String getConstructor(Constructor constructor) {
        String constructorSignature = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        String annotations = new AnnotationUtils().getAnnotations(constructor);

        String indent = new IndentUtils().getIndent(constructor);

        String modifiers = new ModifiersUtils().getModifiers(constructor.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? new GenericsUtils().getGenerics(constructor) : "";

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? constructor.getAnnotatedReturnType() : null;

        String constructorName = new GenericsUtils().resolveType(constructor.getDeclaringClass(), annotatedType);

        String arguments = new ArgumentUtils().getArguments(constructor);

        String exceptions = new ExceptionUtils().getExceptions(constructor);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = " {" + lineSeparator + indent + oneIndent + "/* Compiled code */" + lineSeparator + indent + "}";

        constructorSignature += annotations + indent + modifiers + generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
