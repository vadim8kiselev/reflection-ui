package com.kiselev.reflection.ui.impl.argument;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.modifier.ModifiersUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class ArgumentUtils {

    public String getArguments(Executable executable) {
        String arguments = "";

        ArrayList<String> strings = new ArrayList<>();

        AnnotatedType[] annotatedParameterTypes = executable.getAnnotatedParameterTypes();
        Parameter[] parameters = executable.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            strings.add(getArgument(parameters[index], annotatedParameterTypes[index]));
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter, AnnotatedType annotatedType) {
        String argumentSignature = "";

        String annotations = getArgumentAnnotations(parameter);

        String genericType = new GenericsUtils().resolveType(parameter.getParameterizedType(), annotatedType);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String modifiers = new ModifiersUtils().getModifiers(parameter.getModifiers());

        String parameterName = parameter.getName();

        argumentSignature += annotations + modifiers + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String getArgumentAnnotations(Parameter parameter) {
        String annotations = new AnnotationUtils().getInlineAnnotations(parameter);
        return !annotations.isEmpty() ? annotations + " " : "";
    }

    private String convertToVarArg(String type) {
        return type.substring(0, type.length() - 2) + "...";
    }
}
