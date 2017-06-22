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

        for (int i = 0; i < parameters.length; i++) {
            strings.add(getArgument(parameters[i], annotatedParameterTypes[i]));
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter, AnnotatedType annotatedType) {
        String argumentSignature = "";

        String annotation = new AnnotationUtils().getInlineAnnotations(parameter);

        String genericType = new GenericsUtils().resolveType(parameter.getParameterizedType(), annotatedType);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String modifiers = new ModifiersUtils().getModifiers(parameter.getModifiers());

        String parameterName = parameter.getName(); // TODO : -parameters check

        argumentSignature += (!annotation.isEmpty() ? annotation + " " : "")
                + modifiers + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String convertToVarArg(String type) {
        return type.substring(0, type.length() - 2) + "...";
    }
}
