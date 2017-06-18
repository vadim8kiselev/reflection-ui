package com.kiselev.reflection.ui.impl.generic;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericsUtils {

    public String getGenerics(GenericDeclaration genericDeclaration) {
        return getGenerics(genericDeclaration, null);
    }

    public String getGenerics(GenericDeclaration genericDeclaration, Class<?> parsedClass) {
        List<String> generics = new ArrayList<>();

        String whitespace = (genericDeclaration instanceof Class) ? " " : "";

        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
        for (TypeVariable parameter : typeParameters) {
            String bounds = String.join(" & ", getBounds(parameter, parsedClass));
            String annotations = new AnnotationUtils().getInlineAnnotations(parameter, parsedClass);

            generics.add(annotations + parameter.getName() + (!bounds.isEmpty() ? " extends " + bounds : ""));
        }

        return (typeParameters.length != 0) ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    private List<String> getBounds(TypeVariable parameter, Class<?> parsedClass) {
        List<String> bounds = new ArrayList<>();
        Type[] typeBounds = parameter.getBounds();
        AnnotatedType[] annotatedBounds = parameter.getAnnotatedBounds();

        for (int i = 0; i < typeBounds.length; i++) {
            String annotations = new AnnotationUtils().getInlineAnnotations(annotatedBounds[i], parsedClass);

            String boundType = resolveType(typeBounds[i], parsedClass);
            if (!boundType.isEmpty()) {
                bounds.add(annotations + boundType);
            }
        }
        return bounds;
    }

    public String resolveType(Type type, Class<?> parsedClass) {
        String boundType = "";

        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);
            boundType = new NameUtils().getTypeName(clazz, parsedClass);

        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = TypeVariable.class.cast(type);
            boundType = typeVariable.getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            String parametrizedRawTypeName = new NameUtils().getTypeName(Class.class.cast(parameterizedType.getRawType()), parsedClass);

            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType, parsedClass)) + ">";

            boundType = parametrizedRawTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            boundType = resolveType(genericArrayType.getGenericComponentType(), parsedClass);
            boundType += "[]";
        }

        return boundType;
    }

    public String resolveType(Type type) {
        return resolveType(type, null);
    }

    private List<String> getGenericArguments(ParameterizedType parameterizedType, Class<?> parsedClass) {
        List<String> genericArguments = new ArrayList<>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof WildcardType) {
                WildcardType wildcardType = WildcardType.class.cast(actualTypeArgument);
                String wildcard = "?";
                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends", parsedClass);
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super", parsedClass);
                genericArguments.add(wildcard);
            } else {
                genericArguments.add(resolveType(actualTypeArgument, parsedClass));
            }
        }

        return genericArguments;
    }

    private String getWildCardsBound(Type[] types, String boundCase, Class<?> parsedClass) {
        String wildcard = "";
        if (types.length != 0) {
            wildcard = " " + boundCase + " ";
            List<String> bounds = new ArrayList<>();
            for (Type bound : types) {
                bounds.add(resolveType(bound, parsedClass));
            }
            wildcard += String.join(" & ", bounds);
        }
        return wildcard;
    }
}
