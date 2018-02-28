package com.kiselev.classparser.impl.reflection.generic;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.constants.Cast;
import com.kiselev.classparser.impl.reflection.name.NameUtils;
import com.kiselev.classparser.impl.reflection.packages.PackageUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;

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

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private NameUtils nameUtils = new NameUtils();

    private PackageUtils packageUtils = new PackageUtils();

    public String getGenerics(GenericDeclaration genericDeclaration) {
        List<String> generics = new ArrayList<>();

        String whitespace = (genericDeclaration instanceof Class) ? " " : "";

        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
        for (TypeVariable parameter : typeParameters) {
            String annotations = annotationUtils.getInlineAnnotations(parameter);
            String boundTypes = String.join(" & ", getBounds(parameter));
            String bounds = !boundTypes.isEmpty() ? " extends " + boundTypes : "";

            generics.add(getCorrectAnnotations(annotations) + parameter.getName() + bounds);
        }

        return typeParameters.length != 0 ? "<" + String.join(", ", generics) + "> " : whitespace;
    }

    @SuppressWarnings("ConstantConditions")
    public String resolveType(Type type, AnnotatedType annotatedType) {
        String annotations = "";
        String boundType = "";
        if (annotatedType != null && !isArray(type)) {
            // If type is inner nested class then "use type" annotations for parametrized type is invisible :(
            annotations = annotationUtils.getInlineAnnotations(getAnnotatedType(annotatedType));
        }

        if (type instanceof Class) {
            Class clazz = Cast.CLASS.cast(type);

            if (clazz.isArray()) {
                AnnotatedArrayType annotatedArrayType = Cast.ANNOTATED_ARRAY_TYPE.cast(annotatedType);
                boundType = resolveType(clazz.getComponentType(), annotatedArrayType);
                AnnotatedType annotatedForArrayType = getAnnotatedTypeForArray(clazz, annotatedArrayType);
                boundType += annotationUtils.getInlineAnnotations(annotatedForArrayType) + "[]";
            } else {
                if (isNeedNameForInnerClass(clazz)) {
                    String typeName = resolveType(clazz.getDeclaringClass(), null);
                    boundType = !typeName.isEmpty() ? typeName + "." + getCorrectAnnotations(annotations) : "";
                    annotations = "";
                }

                boundType += nameUtils.getTypeName(clazz);

                if (!clazz.isMemberClass() && boundType.contains(".") && !annotations.isEmpty()) {
                    String packageName = packageUtils.getPackageName(clazz);
                    String simpleName = nameUtils.getSimpleName(clazz);
                    boundType = packageName + "." + annotations + " " + simpleName;
                    annotations = "";
                }
            }
        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = Cast.TYPE_VARIABLE.cast(type);
            boundType = typeVariable.getName();

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = Cast.PARAMETERIZED_TYPE.cast(type);
            if (isNeedNameForInnerClass(Cast.CLASS.cast(parameterizedType.getRawType()))) {
                // Have problems because of https://bugs.openjdk.java.net/browse/JDK-8146861
                AnnotatedParameterizedType annotatedOwnerParametrizedType = null;
                String correctAnnotations = getCorrectAnnotations(annotations);
                Type ownerType = parameterizedType.getOwnerType();
                boundType = resolveType(ownerType, annotatedOwnerParametrizedType) + "." + correctAnnotations;
                annotations = "";
            }

            String genericArguments = "";
            Class<?> clazz = Cast.CLASS.cast(parameterizedType.getRawType());
            String parametrizedRawTypeName = nameUtils.getTypeName(clazz);
            annotatedType = getAnnotatedType(annotatedType);
            AnnotatedParameterizedType annotatedParameterizedType = Cast.ANNOTATED_PARAMETERIZED_TYPE.cast(annotatedType);

            List<String> innerGenericTypes = getGenericArguments(parameterizedType, annotatedParameterizedType);
            if (!innerGenericTypes.isEmpty()) {
                genericArguments = "<" + String.join(", ", innerGenericTypes) + ">";
            }
            boundType += parametrizedRawTypeName + genericArguments;

        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = Cast.GENERIC_ARRAY_TYPE.cast(type);
            AnnotatedArrayType annotatedArrayType = Cast.ANNOTATED_ARRAY_TYPE.cast(annotatedType);
            boundType = resolveType(genericArrayType.getGenericComponentType(), annotatedArrayType);
            AnnotatedType annotatedTypeForArray = getAnnotatedTypeForArray(genericArrayType, annotatedArrayType);
            boundType += annotationUtils.getInlineAnnotations(annotatedTypeForArray) + "[]";
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
            String annotations = annotationUtils.getInlineAnnotations(annotatedBounds[index]);

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
            array = Cast.GENERIC_ARRAY_TYPE.cast(array.getGenericComponentType());
            dimensionIndex++;
        }

        return getAnnotatedType(annotatedType, dimensionIndex);
    }

    private AnnotatedType getAnnotatedType(AnnotatedArrayType annotatedType, int countIncludes) {
        for (int index = 0; index < countIncludes; index++) {
            annotatedType = Cast.ANNOTATED_ARRAY_TYPE.cast(annotatedType.getAnnotatedGenericComponentType());
        }

        return annotatedType;
    }

    private AnnotatedType getAnnotatedType(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedArrayType) {
            AnnotatedArrayType annotatedArrayType = Cast.ANNOTATED_ARRAY_TYPE.cast(annotatedType);
            while (annotatedArrayType.getAnnotatedGenericComponentType() instanceof AnnotatedArrayType) {
                annotatedArrayType = Cast.ANNOTATED_ARRAY_TYPE
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
        AnnotatedType[] annotatedActualTypeArguments = ifNull(annotatedParameterizedType);

        for (int index = 0; index < actualTypeArguments.length; index++) {
            if (actualTypeArguments[index] instanceof WildcardType) {
                WildcardType wildcardType = Cast.WILDCARD_TYPE.cast(actualTypeArguments[index]);
                AnnotatedType annotatedType = ifEmpty(annotatedActualTypeArguments, index);
                AnnotatedWildcardType annotatedWildcardType = Cast.ANNOTATED_WILDCARD_TYPE.cast(annotatedType);
                String annotations = annotationUtils.getInlineAnnotations(annotatedWildcardType);
                String wildcard = getCorrectAnnotations(annotations) + "?";

                AnnotatedType[] upper = ifNullUpper(annotatedWildcardType);
                AnnotatedType[] lower = ifNullLower(annotatedWildcardType);

                wildcard += getWildCardsBound(wildcardType.getUpperBounds(), "extends", upper);
                wildcard += getWildCardsBound(wildcardType.getLowerBounds(), "super", lower);
                genericArguments.add(wildcard);
            } else {
                AnnotatedType annotatedType = ifEmpty(annotatedActualTypeArguments, index);
                genericArguments.add(resolveType(actualTypeArguments[index], annotatedType));
            }
        }

        return genericArguments;
    }

    private String getWildCardsBound(Type[] types, String boundCase, AnnotatedType[] annotatedTypes) {
        String wildcard = "";
        if (types.length != 0) {
            wildcard = " " + boundCase + " ";
            List<String> bounds = new ArrayList<>();
            for (int index = 0; index < types.length; index++) {
                bounds.add(resolveType(types[index], ifEmpty(annotatedTypes, index)));
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

    private boolean isArray(Type type) {
        return type instanceof Class &&
                Cast.CLASS.cast(type).isArray() ||
                type instanceof GenericArrayType;
    }

    private AnnotatedType[] ifNull(AnnotatedParameterizedType type) {
        return type != null ? type.getAnnotatedActualTypeArguments() : null;
    }

    private AnnotatedType ifEmpty(AnnotatedType[] annotatedTypes, int index) {
        return annotatedTypes != null && annotatedTypes.length > 0 ? annotatedTypes[index] : null;
    }

    private AnnotatedType[] ifNullUpper(AnnotatedWildcardType type) {
        return type != null ? type.getAnnotatedUpperBounds() : null;
    }

    private AnnotatedType[] ifNullLower(AnnotatedWildcardType type) {
        return type != null ? type.getAnnotatedLowerBounds() : null;
    }
}
