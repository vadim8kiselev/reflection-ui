package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorParser {

    public static String getConstructors(Class<?> clazz) {
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

    private static String getConstructor(Constructor constructor) {
        String constructorSignature = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        String annotations = AnnotationParser.getAnnotations(constructor);

        String indent = IndentParser.getIndent(constructor);

        String modifiers = ModifierParser.getModifiers(constructor.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? GenericTypeParser.getGenerics(constructor) : "";

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? constructor.getAnnotatedReturnType() : null;

        String constructorName = GenericTypeParser.resolveType(constructor.getDeclaringClass(), annotatedType);

        String arguments = ArgumentParser.getArguments(constructor);

        String exceptions = ExceptionParser.getExceptions(constructor);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = " {" + lineSeparator + indent + oneIndent +
                "/* Compiled code */" + lineSeparator + indent + '}';

        constructorSignature += annotations + indent + modifiers +
                generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
