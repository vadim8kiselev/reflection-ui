package com.kiselev.reflection.ui.impl.argument;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArgumentUtils {

    public String getArguments(Executable executable) {
        String arguments = "";

        ArrayList<String> strings = new ArrayList<>();

        AnnotatedType[] annotatedParameterTypes = executable.getAnnotatedParameterTypes();
        Parameter[] parameters = executable.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            strings.add(getArgument(parameters[i], annotatedParameterTypes[i],  executable.getDeclaringClass()));
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter, AnnotatedType annotatedType, Class<?> parsedClass) {
        String argumentSignature = "";

        String annotation = new AnnotationUtils().getInlineAnnotations(parameter, parsedClass);

        String genericType = new GenericsUtils().resolveType(parameter.getParameterizedType(),
                annotatedType, parsedClass);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String parameterName = parameter.getName(); // TODO : -parameters check

        argumentSignature += annotation + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String convertToVarArg(String type) {
        return type.substring(0, type.length() - 2) + "...";
    }
}
