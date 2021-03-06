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
            strings.add(getArgument(parameter));
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter) {
        String argumentSignature = "";

        String annotation = new AnnotationUtils().getAnnotations(parameter);

        String genericType = new GenericsUtils().resolveType(parameter.getParameterizedType());

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String parameterName = parameter.getName(); // TODO : -parameters check

        argumentSignature += annotation + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String convertToVarArg(String type) {
        return type.substring(0, type.length() - 2) + "...";
    }
}
