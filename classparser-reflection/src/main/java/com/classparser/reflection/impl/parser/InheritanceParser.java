package com.classparser.reflection.impl.parser;

import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InheritanceParser {

    public static String getInheritances(Class<?> clazz) {
        String inheritances = "";

        inheritances += getSuperClass(clazz);

        inheritances += getInterfaces(clazz);

        return inheritances;
    }

    private static String getSuperClass(Class<?> clazz) {
        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? clazz.getAnnotatedSuperclass() : null;

        String superClass;
        if (StateManager.getConfiguration().isShowGenericSignatures()) {
            superClass = getSingleParentType(clazz.getGenericSuperclass(), annotatedType);
        } else {
            superClass = getSingleParentType(clazz.getSuperclass(), annotatedType);
        }

        return (!superClass.isEmpty()) ? "extends " + superClass + ' ' : "";
    }

    private static String getInterfaces(Class<?> clazz) {
        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();
        AnnotatedType[] annotatedTypes = isShowTypeAnnotation ? clazz.getAnnotatedInterfaces() : null;
        String interfaces;
        if (StateManager.getConfiguration().isShowGenericSignatures()) {
            interfaces = String.join(", ",
                    getMultipleParentTypes(clazz.getGenericInterfaces(), annotatedTypes));
        } else {
            interfaces = String.join(", ",
                    getMultipleParentTypes(clazz.getInterfaces(), annotatedTypes));
        }

        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + ' ' : "";
    }

    private static List<String> getMultipleParentTypes(Type[] parentTypes, AnnotatedType[] annotatedTypes) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (int index = 0; index < parentTypes.length; index++) {
            multipleParentTypes.add(getSingleParentType(parentTypes[index], ifEmpty(annotatedTypes, index)));
        }
        return multipleParentTypes;
    }

    private static String getSingleParentType(Type parentType, AnnotatedType annotatedType) {
        return GenericTypeParser.resolveType(parentType, annotatedType);
    }

    private static AnnotatedType ifEmpty(AnnotatedType[] types, int index) {
        return types == null || types.length == 0 ? null : types[index];
    }
}
