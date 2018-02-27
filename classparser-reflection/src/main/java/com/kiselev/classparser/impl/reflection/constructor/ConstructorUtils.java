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

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private IndentUtils indentUtils = new IndentUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private ArgumentUtils argumentUtils = new ArgumentUtils();

    private ExceptionUtils exceptionUtils = new ExceptionUtils();

    private ModifiersUtils modifiersUtils = new ModifiersUtils();

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

        String annotations = annotationUtils.getAnnotations(constructor);

        String indent = indentUtils.getIndent(constructor);

        String modifiers = modifiersUtils.getModifiers(constructor.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericsUtils.getGenerics(constructor) : "";

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? constructor.getAnnotatedReturnType() : null;

        String constructorName = genericsUtils.resolveType(constructor.getDeclaringClass(), annotatedType);

        String arguments = argumentUtils.getArguments(constructor);

        String exceptions = exceptionUtils.getExceptions(constructor);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = " {" + lineSeparator + indent + oneIndent +
                "/* Compiled code */" + lineSeparator + indent + "}";

        constructorSignature += annotations + indent + modifiers +
                generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
