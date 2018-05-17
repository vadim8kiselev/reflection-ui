package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArgumentParser {

    private final ReflectionParserManager manager;

    private final GenericTypeParser genericTypeParser;

    private final ModifierParser modifierParser;

    private final AnnotationParser annotationParser;

    public ArgumentParser(ReflectionParserManager manager, GenericTypeParser genericTypeParser,
                          ModifierParser modifierParser, AnnotationParser annotationParser) {
        this.manager = manager;
        this.genericTypeParser = genericTypeParser;
        this.modifierParser = modifierParser;
        this.annotationParser = annotationParser;
    }

    public String getArguments(Executable executable) {
        String arguments = "";

        ArrayList<String> strings = new ArrayList<>();

        AnnotatedType[] annotatedParameterTypes = executable.getAnnotatedParameterTypes();

        Parameter[] parameters = executable.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            if (manager.getConfigurationManager().isShowAnnotationTypes()) {
                strings.add(getArgument(parameters[index], annotatedParameterTypes[index]));
            } else {
                strings.add(getArgument(parameters[index], null));
            }
        }

        arguments += '(' + String.join(", ", strings) + ')';

        return arguments;
    }

    private String getArgument(Parameter parameter, AnnotatedType annotatedType) {
        String argumentSignature = "";

        String annotations = getArgumentAnnotations(parameter);

        boolean isShowGeneric = manager.getConfigurationManager().isShowGenericSignatures();

        Type type = isShowGeneric ? parameter.getParameterizedType() : parameter.getType();

        String genericType = genericTypeParser.resolveType(type, annotatedType);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String modifiers = modifierParser.getModifiers(parameter.getModifiers());

        String parameterName = parameter.getName();

        argumentSignature += annotations + modifiers + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String getArgumentAnnotations(Parameter parameter) {
        String annotations = annotationParser.getInlineAnnotations(parameter);
        return !annotations.isEmpty() ? annotations + ' ' : "";
    }

    private String convertToVarArg(String type) {
        if (manager.getConfigurationManager().isShowVarArgs()) {
            return type.substring(0, type.length() - 2) + "...";
        } else {
            return type;
        }
    }
}
