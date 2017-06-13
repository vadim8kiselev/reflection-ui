package com.kiselev.reflection.ui.impl.generic;

import com.kiselev.reflection.ui.impl.name.NameUtils;

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
            boundType = new NameUtils().getTypeName(clazz);

        } else if (type instanceof TypeVariable) {
            boundType = TypeVariable.class.cast(type).getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            String parameterizedTypeTypeName = new NameUtils().getTypeName(Class.class.cast(parameterizedType.getRawType()));

            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType)) + ">";

            boundType = parameterizedTypeTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            boundType = resolveType(genericArrayType.getGenericComponentType());

            for (int index = 0; index < getArrayDimension(genericArrayType); index++) {
                boundType += "[]";
            }
        }

        return boundType;
    }

    private Integer getArrayDimension(GenericArrayType arrayType) {
        Integer arrayDimension = 1;

        Type outerType = arrayType;
        while ((outerType = ((GenericArrayType) outerType).getGenericComponentType()) instanceof GenericArrayType) {
            arrayDimension++;
        }

        return arrayDimension;
    }

    private List<String> getGenericArguments(ParameterizedType parameterizedType) {
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
