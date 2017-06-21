package com.kiselev.reflection.ui.impl.generic;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.imports.ManagerImportUtils;
import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
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
        List<String> generics = new ArrayList<>();

        String whitespace = (genericDeclaration instanceof Class) ? " " : "";

        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
        for (TypeVariable parameter : typeParameters) {
            String bounds = String.join(" & ", getBounds(parameter));
            String annotations = new AnnotationUtils().getInlineAnnotations(parameter);

            generics.add((annotations.isEmpty() ? "" : annotations + " ")
                    + parameter.getName() + (!bounds.isEmpty() ? " extends " + bounds : ""));
        }

        return (typeParameters.length != 0) ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    private List<String> getBounds(TypeVariable parameter) {
        List<String> bounds = new ArrayList<>();
        Type[] typeBounds = parameter.getBounds();
        AnnotatedType[] annotatedBounds = parameter.getAnnotatedBounds();

        for (int i = 0; i < typeBounds.length; i++) {
            String annotations = new AnnotationUtils().getInlineAnnotations(annotatedBounds[i]);

            String boundType = resolveType(typeBounds[i]);
            if (!boundType.isEmpty()) {
                bounds.add((annotations.isEmpty() ? "" : annotations + " ") + boundType);
            }
        }
        return bounds;
    }

    //If type is inner nested class then type use annotation is invisible :(
    public String resolveType(Type type, AnnotatedType annotatedType) {
        String annotations = "";
        String boundType = "";
        if (annotatedType != null && !(type instanceof Class
                && ((Class) type).isArray() || type instanceof GenericArrayType)) {
            annotations = new AnnotationUtils().getInlineAnnotations(getAnnotatedType(annotatedType));
        }

        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);

            if (clazz.isArray()) {
                AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
                boundType = resolveType(clazz.getComponentType(), annotatedArrayType);
                boundType += new AnnotationUtils()
                        .getInlineAnnotations(getAnnotatedTypeForArray(clazz, annotatedArrayType)) + "[]";
            } else {
                if (isNeedNameForInnerClass(clazz)) {
                    String typeName = resolveType(clazz.getDeclaringClass(), null);
                    boundType = typeName.isEmpty() ? "" : typeName + ".";
                }

                boundType += new NameUtils().getTypeName(clazz);
                if (boundType.contains(".") && !annotations.isEmpty()) {
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
            if (isNeedNameForInnerClass(Class.class.cast(parameterizedType.getRawType()))) {
                //Have problems because of https://bugs.openjdk.java.net/browse/JDK-8146861
                boundType = resolveType(parameterizedType.getOwnerType(), null) + ".";
            }

            String parametrizedRawTypeName = new NameUtils()
                    .getTypeName(Class.class.cast(parameterizedType.getRawType()));

            String genericArguments = "<" + String.join(", ", getGenericArguments(parameterizedType,
                    AnnotatedParameterizedType.class.cast(getAnnotatedType(annotatedType)))) + ">";

            boundType += parametrizedRawTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
            boundType = resolveType(genericArrayType.getGenericComponentType(), annotatedArrayType);
            boundType += new AnnotationUtils().getInlineAnnotations(getAnnotatedTypeForArray(genericArrayType,
                    annotatedArrayType)) + "[]";
        }

        return (!annotations.isEmpty() ? annotations + " " : "") + boundType;
    }

    public String resolveType(Type type) {
        return resolveType(type, null);
    }

    private AnnotatedType getAnnotatedTypeForArray(Class<?> array, AnnotatedArrayType annotatedType) {
        int dimensionIndex = 0;
        while (array.getComponentType().isArray()) {
            array = array.getComponentType();
            dimensionIndex++;
        }

        return getAnnotatedType(annotatedType, dimensionIndex);
    }

    private AnnotatedType getAnnotatedTypeForArray(GenericArrayType array, AnnotatedArrayType annotatedType) {
        int dimensionIndex = 0;
        while (array.getGenericComponentType() instanceof GenericArrayType) {
            array = GenericArrayType.class.cast(array.getGenericComponentType());
            dimensionIndex++;
        }

        return getAnnotatedType(annotatedType, dimensionIndex);
    }

    private AnnotatedType getAnnotatedType(AnnotatedArrayType annotatedType, int countIncludes) {
        for (int i = 0; i < countIncludes; i++) {
            annotatedType = AnnotatedArrayType.class.cast(annotatedType.getAnnotatedGenericComponentType());
        }

        return annotatedType;
    }

    private AnnotatedType getAnnotatedType(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedArrayType) {
            AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
            while (annotatedArrayType.getAnnotatedGenericComponentType() instanceof AnnotatedArrayType) {
                annotatedArrayType = AnnotatedArrayType.class
                        .cast(annotatedArrayType.getAnnotatedGenericComponentType());
            }
            annotatedType = annotatedArrayType.getAnnotatedGenericComponentType();
        }

        return annotatedType;
    }

    private List<String> getGenericArguments(ParameterizedType parameterizedType,
                                             AnnotatedParameterizedType annotatedParameterizedType) {
        List<String> genericArguments = new ArrayList<>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        AnnotatedType[] annotatedActualTypeArguments;
        if (annotatedParameterizedType != null) {
            annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();
        } else {
            annotatedActualTypeArguments = new AnnotatedType[0];
        }

        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (actualTypeArguments[i] instanceof WildcardType) {
                WildcardType wildcardType = WildcardType.class.cast(actualTypeArguments[i]);
                AnnotatedWildcardType annotatedWildcardType = AnnotatedWildcardType.class
                        .cast(annotatedActualTypeArguments[i]);
                String wildcard = "?";
                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends",
                        annotatedWildcardType.getAnnotatedUpperBounds());
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super",
                        annotatedWildcardType.getAnnotatedLowerBounds());
                genericArguments.add(wildcard);
            } else {
                if (annotatedActualTypeArguments.length == 0) {
                    genericArguments.add(resolveType(actualTypeArguments[i], null));
                } else {
                    genericArguments.add(resolveType(actualTypeArguments[i], annotatedActualTypeArguments[i]));
                }
            }
        }

        return genericArguments;
    }

    private String getWildCardsBound(Type[] types, String boundCase, AnnotatedType[] annotatedTypes) {
        String wildcard = "";
        if (types.length != 0) {
            wildcard = " " + boundCase + " ";
            List<String> bounds = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                if (annotatedTypes.length != 0) {
                    bounds.add(resolveType(types[i], annotatedTypes[i]));
                } else {
                    bounds.add(resolveType(types[i], null));
                }
            }
            wildcard += String.join(" & ", bounds);
        }
        return wildcard;
    }

    private boolean isNeedNameForInnerClass(Class<?> innerClass) {
        return innerClass.isMemberClass()
                && (!getTopClass(innerClass).equals(getTopClass(ManagerImportUtils.getImportUtils().getParsedClass()))
                || !isInVisibilityZone(innerClass));
    }

    private Class<?> getTopClass(Class<?> innerClass) {
        if (innerClass.getDeclaringClass() == null) {
            return innerClass;
        } else {
            return getTopClass(innerClass.getDeclaringClass());
        }
    }

    private boolean isInVisibilityZone(Class<?> innerClass) {
        Class<?> currentClass = ManagerImportUtils.getImportUtils().getCurrentClass();
        while (currentClass != null) {
            List<Class<?>> innerClasses = Arrays.asList(currentClass.getDeclaredClasses());
            if (innerClasses.contains(innerClass)) {
                return true;
            }

            currentClass = currentClass.getDeclaringClass();
        }

        return false;
    }
}
