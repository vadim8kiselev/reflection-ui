package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorParser {

    private AnnotationParser annotationParser = new AnnotationParser();

    private IndentParser indentParser = new IndentParser();

    private GenericTypeParser genericTypeParser = new GenericTypeParser();

    private ArgumentParser argumentParser = new ArgumentParser();

    private ExceptionParser exceptionParser = new ExceptionParser();

    private ModifierParser modifierParser = new ModifierParser();

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

        String annotations = annotationParser.getAnnotations(constructor);

        String indent = indentParser.getIndent(constructor);

        String modifiers = modifierParser.getModifiers(constructor.getModifiers());

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericTypeParser.getGenerics(constructor) : "";

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? constructor.getAnnotatedReturnType() : null;

        String constructorName = genericTypeParser.resolveType(constructor.getDeclaringClass(), annotatedType);

        String arguments = argumentParser.getArguments(constructor);

        String exceptions = exceptionParser.getExceptions(constructor);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = " {" + lineSeparator + indent + oneIndent +
                "/* Compiled code */" + lineSeparator + indent + "}";

        constructorSignature += annotations + indent + modifiers +
                generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
