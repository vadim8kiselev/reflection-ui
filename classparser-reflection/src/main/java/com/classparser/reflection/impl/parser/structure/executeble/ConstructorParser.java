package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.state.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorParser {

    private final ReflectionParserManager manager;

    private final GenericTypeParser genericTypeParser;

    private final ModifierParser modifierParser;

    private final AnnotationParser annotationParser;

    private final ArgumentParser argumentParser;

    private final IndentParser indentParser;

    private final ExceptionParser exceptionParser;

    public ConstructorParser(ReflectionParserManager manager, GenericTypeParser genericTypeParser,
                             ModifierParser modifierParser, AnnotationParser annotationParser,
                             ArgumentParser argumentParser, IndentParser indentParser, ExceptionParser exceptionParser) {
        this.manager = manager;
        this.genericTypeParser = genericTypeParser;
        this.modifierParser = modifierParser;
        this.annotationParser = annotationParser;
        this.argumentParser = argumentParser;
        this.indentParser = indentParser;
        this.exceptionParser = exceptionParser;
    }

    public String getConstructors(Class<?> clazz) {
        String constructors = "";

        List<String> constructorList = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructorList.add(getConstructor(constructor));
        }

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (!constructorList.isEmpty()) {
            constructors += String.join(lineSeparator + lineSeparator, constructorList) + lineSeparator;
        }

        return constructors;
    }

    private String getConstructor(Constructor constructor) {
        String constructorSignature = "";

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        String annotations = annotationParser.getAnnotations(constructor);

        String indent = indentParser.getIndent(constructor);

        String modifiers = modifierParser.getModifiers(constructor.getModifiers());

        boolean isShowGeneric = manager.getConfigurationManager().isShowGenericSignatures();

        String generics = isShowGeneric ? genericTypeParser.getGenerics(constructor) : "";

        boolean isShowTypeAnnotation = manager.getConfigurationManager().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? constructor.getAnnotatedReturnType() : null;

        String constructorName = genericTypeParser.resolveType(constructor.getDeclaringClass(), annotatedType);

        String arguments = argumentParser.getArguments(constructor);

        String exceptions = exceptionParser.getExceptions(constructor);

        String oneIndent = manager.getConfigurationManager().getIndentSpaces();

        String body = " {" + lineSeparator + indent + oneIndent +
                "/* Compiled code */" + lineSeparator + indent + '}';

        constructorSignature += annotations + indent + modifiers +
                generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
