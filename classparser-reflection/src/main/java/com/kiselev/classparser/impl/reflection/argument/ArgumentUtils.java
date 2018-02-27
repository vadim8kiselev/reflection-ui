package com.kiselev.classparser.impl.reflection.argument;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArgumentUtils {

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private ModifiersUtils modifiersUtils = new ModifiersUtils();

    public String getArguments(Executable executable) {
        String arguments = "";

        ArrayList<String> strings = new ArrayList<>();

        AnnotatedType[] annotatedParameterTypes = executable.getAnnotatedParameterTypes();

        Parameter[] parameters = executable.getParameters();

        for (int index = 0; index < parameters.length; index++) {
            if (StateManager.getConfiguration().isShowAnnotationTypes()) {
                strings.add(getArgument(parameters[index], annotatedParameterTypes[index]));
            } else {
                strings.add(getArgument(parameters[index], null));
            }
        }

        arguments += "(" + String.join(", ", strings) + ")";

        return arguments;
    }

    private String getArgument(Parameter parameter, AnnotatedType annotatedType) {
        String argumentSignature = "";

        String annotations = getArgumentAnnotations(parameter);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type type = isShowGeneric ? parameter.getParameterizedType() : parameter.getType();

        String genericType = genericsUtils.resolveType(type, annotatedType);

        genericType = parameter.isVarArgs() ? convertToVarArg(genericType) : genericType;

        String modifiers = modifiersUtils.getModifiers(parameter.getModifiers());

        String parameterName = parameter.getName();

        argumentSignature += annotations + modifiers + genericType + " " + parameterName;

        return argumentSignature;
    }

    private String getArgumentAnnotations(Parameter parameter) {
        String annotations = annotationUtils.getInlineAnnotations(parameter);
        return !annotations.isEmpty() ? annotations + " " : "";
    }

    private String convertToVarArg(String type) {
        if (StateManager.getConfiguration().isShowVarArgs()) {
            return type.substring(0, type.length() - 2) + "...";
        } else {
            return type;
        }
    }
}
