package com.kiselev.reflection.ui.impl.reflection.generic;

import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;

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
            String annotations = new AnnotationUtils().getInlineAnnotations(parameter);
            String boundTypes = String.join(" & ", getBounds(parameter));
            String bounds = !boundTypes.isEmpty() ? " extends " + boundTypes : "";

            generics.add(getCorrectAnnotations(annotations) + parameter.getName() + bounds);
        }

        return typeParameters.length != 0 ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    // If type is inner nested class then "use type" annotations is invisible :(
    public String resolveType(Type type, AnnotatedType annotatedType) {
        String annotations = "";
        String boundType = "";
        if (annotatedType != null && !(type instanceof Class && ((Class) type).isArray() || type instanceof GenericArrayType)) {
            annotations = new AnnotationUtils().getInlineAnnotations(getAnnotatedType(annotatedType));
        }

        if (type instanceof Class) {
            Class clazz = Class.class.cast(type);

            if (clazz.isArray()) {
                AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
                boundType = resolveType(clazz.getComponentType(), annotatedArrayType);
                boundType += new AnnotationUtils().getInlineAnnotations(getAnnotatedTypeForArray(clazz, annotatedArrayType)) + "[]";
            } else {
                if (isNeedNameForInnerClass(clazz)) {
                    String typeName = resolveType(clazz.getDeclaringClass(), null);
                    boundType = typeName.isEmpty() ? "" : typeName + Constants.Symbols.DOT + getCorrectAnnotations(annotations);
                    annotations = "";
                }

                boundType += new NameUtils().getTypeName(clazz);

                if (!clazz.isMemberClass() && boundType.contains(Constants.Symbols.DOT) && !annotations.isEmpty()) {
                    Package pack = clazz.getPackage();
                    String packageName = pack != null ? pack.getName() : "";
                    String simpleName = new NameUtils().getSimpleName(clazz);
                    boundType = packageName + Constants.Symbols.DOT + annotations + " " + simpleName;
                    annotations = "";
                }
            }

        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = TypeVariable.class.cast(type);
            boundType = typeVariable.getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            if (isNeedNameForInnerClass(Class.class.cast(parameterizedType.getRawType()))) {

                // Have problems because of https://bugs.openjdk.java.net/browse/JDK-8146861
                AnnotatedParameterizedType annotatedOwnerParametrizedType = null;
                boundType = resolveType(parameterizedType.getOwnerType(),
                        annotatedOwnerParametrizedType) + Constants.Symbols.DOT + getCorrectAnnotations(annotations);
                annotations = "";
            }

            String genericArguments = "";

            String parametrizedRawTypeName = new NameUtils().getTypeName(Class.class.cast(parameterizedType.getRawType()));

            List<String> innerGenericTypes = getGenericArguments(parameterizedType,
                    AnnotatedParameterizedType.class.cast(getAnnotatedType(annotatedType)));

            if (!innerGenericTypes.isEmpty()) {
                genericArguments = "<" + String.join(", ", innerGenericTypes) + ">";
            }
            boundType += parametrizedRawTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
            AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
            boundType = resolveType(genericArrayType.getGenericComponentType(), annotatedArrayType);
            boundType += new AnnotationUtils().getInlineAnnotations(getAnnotatedTypeForArray(genericArrayType, annotatedArrayType)) + "[]";
        }

        return getCorrectAnnotations(annotations) + boundType;
    }

    public String resolveType(Type type) {
        return resolveType(type, null);
    }

    private List<String> getBounds(TypeVariable parameter) {
        List<String> bounds = new ArrayList<>();
        Type[] typeBounds = parameter.getBounds();
        AnnotatedType[] annotatedBounds = parameter.getAnnotatedBounds();

        for (int index = 0; index < typeBounds.length; index++) {
            String annotations = new AnnotationUtils().getInlineAnnotations(annotatedBounds[index]);

            String boundType = resolveType(typeBounds[index]);
            if (!boundType.isEmpty()) {
                bounds.add(getCorrectAnnotations(annotations) + boundType);
            }
        }
        return bounds;
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
        for (int index = 0; index < countIncludes; index++) {
            annotatedType = AnnotatedArrayType.class.cast(annotatedType.getAnnotatedGenericComponentType());
        }

        return annotatedType;
    }

    private AnnotatedType getAnnotatedType(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedArrayType) {
            AnnotatedArrayType annotatedArrayType = AnnotatedArrayType.class.cast(annotatedType);
            while (annotatedArrayType.getAnnotatedGenericComponentType() instanceof AnnotatedArrayType) {
                annotatedArrayType = AnnotatedArrayType.class.cast(annotatedArrayType.getAnnotatedGenericComponentType());
            }
            annotatedType = annotatedArrayType.getAnnotatedGenericComponentType();
        }

        return annotatedType;
    }

    private List<String> getGenericArguments(ParameterizedType parameterizedType, AnnotatedParameterizedType annotatedParameterizedType) {
        List<String> genericArguments = new ArrayList<>();

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType != null
                ? annotatedParameterizedType.getAnnotatedActualTypeArguments() : null;

        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (actualTypeArguments[i] instanceof WildcardType) {
                WildcardType wildcardType = WildcardType.class.cast(actualTypeArguments[i]);
                AnnotatedWildcardType annotatedWildcardType = AnnotatedWildcardType.class.cast(annotatedActualTypeArguments != null ? annotatedActualTypeArguments[i] : null);
                String wildcard = getCorrectAnnotations(new AnnotationUtils().getInlineAnnotations(annotatedWildcardType)) + "?";

                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends",
                        annotatedWildcardType != null ? annotatedWildcardType.getAnnotatedUpperBounds() : null);
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super",
                        annotatedWildcardType != null ? annotatedWildcardType.getAnnotatedUpperBounds() : null);
                genericArguments.add(wildcard);
            } else {
                genericArguments.add(resolveType(actualTypeArguments[i],
                        annotatedActualTypeArguments != null ? annotatedActualTypeArguments[i] : null));
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
                bounds.add(resolveType(types[i], (annotatedTypes == null || annotatedTypes.length == 0 ? null : annotatedTypes[i])));
            }
            wildcard += String.join(" & ", bounds);
        }
        return wildcard;
    }

    private boolean isNeedNameForInnerClass(Class<?> innerClass) {
        Class<?> parsedClass = StateManager.getParsedClass();
        return innerClass.isMemberClass()
                && (!getTopClass(innerClass).equals(getTopClass(parsedClass))
                || !isInVisibilityZone(innerClass));
    }

    private boolean isInVisibilityZone(Class<?> innerClass) {
        Class<?> currentClass = StateManager.getCurrentClass();
        while (currentClass != null) {
            List<Class<?>> innerClasses = Arrays.asList(currentClass.getDeclaredClasses());
            if (innerClasses.contains(innerClass)) {
                return true;
            }

            currentClass = currentClass.getDeclaringClass();
        }
        return false;
    }

    private Class<?> getTopClass(Class<?> innerClass) {
        return innerClass.getDeclaringClass() != null ? getTopClass(innerClass.getDeclaringClass()) : innerClass;
    }

    private String getCorrectAnnotations(String annotations) {
        return !annotations.isEmpty() ? annotations + " " : "";
    }
}
