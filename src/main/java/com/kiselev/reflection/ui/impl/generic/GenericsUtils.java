package com.kiselev.reflection.ui.impl.generic;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.AnnotatedWildcardType;
import java.util.ArrayList;
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

            String boundType = resolveType(typeBounds[i], annotatedBounds[i], parsedClass);
            if (!boundType.isEmpty()) {
                bounds.add(annotations + boundType);
            }
        }
        return bounds;
    }

    public String resolveType(Type type, Class<?> parsedClass) {
        return resolveType(type, null, parsedClass);
    }

    public String resolveType(Type type, AnnotatedType annotatedType, Class<?> parsedClass) {
        String annotations = "";
        String boundType = "";
        if (annotatedType != null) {
            if (!(type instanceof Class && ((Class) type).isArray() || type instanceof GenericArrayType)){
                annotations = new AnnotationUtils().getInlineAnnotations(annotatedType, parsedClass);
            }
        }

        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);

            if (clazz.isArray()) {
                AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
                boundType = resolveType(clazz.getComponentType(), annotatedArrayType.getAnnotatedGenericComponentType(), parsedClass);
                boundType += new AnnotationUtils().getInlineAnnotations(annotatedType, parsedClass) + "[]";
            } else {
                boundType = new NameUtils().getTypeName(clazz, parsedClass);
                if (boundType.contains(".")) {
                    String packageName = clazz.getPackage().getName();
                    String simpleName = new NameUtils().getSimpleName(clazz);
                    boundType = packageName + "." + annotations + " " + simpleName;
                    annotations = "";
                }
            }

        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = TypeVariable.class.cast(type);
            boundType = typeVariable.getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            String parametrizedRawTypeName = new NameUtils().getTypeName(Class.class.cast(parameterizedType.getRawType()), parsedClass);

            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType,
                    AnnotatedParameterizedType.class.cast(annotatedType), parsedClass)) + ">";

            boundType = parametrizedRawTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
            boundType = resolveType(genericArrayType.getGenericComponentType(), annotatedArrayType.getAnnotatedGenericComponentType(), parsedClass);
            boundType += new AnnotationUtils().getInlineAnnotations(annotatedType, parsedClass) + "[]";
        }

        return (!"".equals(annotations) ? annotations + " " : "") + boundType;
    }

    public String resolveType(Type type) {
        return resolveType(type, null);
    }

    private List<String> getGenericArguments(ParameterizedType parameterizedType, AnnotatedParameterizedType annotatedParameterizedType, Class<?> parsedClass) {
        List<String> genericArguments = new ArrayList<>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();

        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (actualTypeArguments[i] instanceof WildcardType) {
                WildcardType wildcardType = WildcardType.class.cast(actualTypeArguments[i]);
                AnnotatedWildcardType annotatedWildcardType = AnnotatedWildcardType.class.cast(annotatedActualTypeArguments[i]);
                String wildcard = "?";
                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends",
                        annotatedWildcardType.getAnnotatedUpperBounds(), parsedClass);
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super",
                        annotatedWildcardType.getAnnotatedLowerBounds(), parsedClass);
                genericArguments.add(wildcard);
            } else {
                genericArguments.add(resolveType(actualTypeArguments[i], annotatedActualTypeArguments[i], parsedClass));
            }
        }

        return genericArguments;
    }

    private String getWildCardsBound(Type[] types, String boundCase, AnnotatedType[] annotatedTypes, Class<?> parsedClass) {
        String wildcard = "";
        if (types.length != 0) {
            wildcard = " " + boundCase + " ";
            List<String> bounds = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                bounds.add(resolveType(types[i], annotatedTypes[i], parsedClass));
            }
            wildcard += String.join(" & ", bounds);
        }
        return wildcard;
    }
}
