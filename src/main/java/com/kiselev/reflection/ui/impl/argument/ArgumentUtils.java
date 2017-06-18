package com.kiselev.reflection.ui.impl.argument;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class ArgumentUtils {

    public String getArguments(Executable executable) {
        String arguments = "";

        ArrayList<String> strings = new ArrayList<>();

        for (Parameter parameter : executable.getParameters()) {
            strings.add(getArgument(parameter, executable.getDeclaringClass()));
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter, Class<?> parsedClass) {
        String argumentSignature = "";

        String annotation = new AnnotationUtils().getAnnotations(parameter, parsedClass);
        if (!"".equals(annotation)) {
            annotation = annotation.substring(0, annotation.length() - 1);
        }

        String genericType = new GenericsUtils().resolveType(parameter.getParameterizedType(), parsedClass);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String parameterName = parameter.getName(); // TODO : -parameters check

        argumentSignature += annotation + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String convertToVarArg(String type) {
        return type.substring(0, type.length() - 2) + "...";
    }
}
