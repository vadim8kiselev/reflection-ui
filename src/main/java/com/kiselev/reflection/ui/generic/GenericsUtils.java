package com.kiselev.reflection.ui.generic;

import com.kiselev.reflection.ui.name.NameUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

public class GenericsUtils {

    public String getGenerics(GenericDeclaration genericDeclaration) {
        List<String> generics = new ArrayList<>();

        String whitespace = (genericDeclaration instanceof Class) ? " " : "";

        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
        for (TypeVariable parameter : typeParameters) {
            String bounds = String.join(" & ", getBounds(parameter));
            generics.add(parameter.getName() + (!bounds.isEmpty() ? " extends " + bounds : ""));
        }

        return (typeParameters.length != 0) ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    private List<String> getBounds(TypeVariable parameter) {
        List<String> bounds = new ArrayList<>();
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

        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);
            if (clazz.isSynthetic()) {
                boundType = new NameUtils().getTypeName(clazz);
            } else {
                boundType = clazz.getSimpleName();
            }

        } else if (type instanceof TypeVariable) {
            boundType = TypeVariable.class.cast(type).getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.class.cast(type);

            String parameterizedTypeTypeName = parameterizedType.getRawType().getSimpleName();

            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType)) + ">";

            boundType = parameterizedTypeTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            boundType = genericArrayType.getGenericComponentType().getTypeName();
            int arrayMeasuring = (genericArrayType.getTypeName().length() - boundType.length())/2;
            for (int i = 0; i < arrayMeasuring; i++) {
                boundType += "[]";
            }
        }

        return boundType;
    }

    private List<String> getGenericArguments(ParameterizedTypeImpl parameterizedType) {
        List<String> genericArguments = new ArrayList<>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof WildcardType) {
                WildcardType wildcardType = WildcardType.class.cast(actualTypeArgument);
                String wildcard = "?";
                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends");
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super");
                genericArguments.add(wildcard);
            } else {
                genericArguments.add(resolveType(actualTypeArgument));
            }
        }

        return genericArguments;
    }

    private String getWildCardsBound(Type[] types, String boundCase) {
        String wildcard = "";
        if (types.length != 0) {
            wildcard = " " + boundCase + " ";
            List<String> bounds = new ArrayList<>();
            for (Type bound : types) {
                bounds.add(resolveType(bound));
            }
            wildcard += String.join(" & ", bounds);
        }
        return wildcard;
    }
}
