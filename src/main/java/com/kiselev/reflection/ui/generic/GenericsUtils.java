package com.kiselev.reflection.ui.generic;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class GenericsUtils {

    public String getGenerics(GenericDeclaration genericDeclaration) {
        List<String> generics = new ArrayList<String>();

        String whitespace = (genericDeclaration instanceof Class) ? " " : "";

        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
        for (TypeVariable parameter : typeParameters) {
            String bounds = String.join(" & ", getBounds(parameter));
            generics.add(parameter.getName() + (!bounds.isEmpty() ? " extends " + bounds : "" ));
        }

        return (typeParameters.length != 0) ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    private List<String> getBounds(TypeVariable parameter) {
        List<String> bounds = new ArrayList<String>();
        for (Type bound : parameter.getBounds()) {
            String boundType = resolveType(bound);
            if (!boundType.isEmpty()) {
                bounds.add(boundType);
            }
        }
        return bounds;
    }

    public String resolveType(Type type) {
        String boundType = "";

        String whitespace = (type instanceof Class) ? " " : "";

        if (type instanceof Class) {
            boundType = Class.class.cast(type).getSimpleName();

        } else if (type instanceof TypeVariable) {
            boundType = TypeVariable.class.cast(type).getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.class.cast(type);

            String parameterizedTypeTypeName = parameterizedType.getRawType().getSimpleName();
            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType)) + ">";

            boundType = parameterizedTypeTypeName + genericArguments;
        }

        return boundType;
    }

    private List<String> getGenericArguments(ParameterizedTypeImpl parameterizedType) {
        List<String> genericArguments = new ArrayList<String>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            genericArguments.add(resolveType(actualTypeArgument));
        }

        return genericArguments;
    }
}
