package com.classparser.reflection.impl.parser;

import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InheritanceParser {

    private final GenericTypeParser genericTypeParser;

    private final ReflectionParserManager manager;

    public InheritanceParser(GenericTypeParser genericTypeParser, ReflectionParserManager manager) {
        this.genericTypeParser = genericTypeParser;
        this.manager = manager;
    }

    public String getInheritances(Class<?> clazz) {
        String inheritances = "";

        inheritances += getSuperClass(clazz);

        inheritances += getInterfaces(clazz);

        return inheritances;
    }

    private String getSuperClass(Class<?> clazz) {
        boolean isShowTypeAnnotation = manager.getConfigurationManager().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? clazz.getAnnotatedSuperclass() : null;

        String superClass;
        if (manager.getConfigurationManager().isShowGenericSignatures()) {
            superClass = getSingleParentType(clazz.getGenericSuperclass(), annotatedType);
        } else {
            superClass = getSingleParentType(clazz.getSuperclass(), annotatedType);
        }

        return (!superClass.isEmpty()) ? "extends " + superClass + ' ' : "";
    }

    private String getInterfaces(Class<?> clazz) {
        boolean isShowTypeAnnotation = manager.getConfigurationManager().isShowAnnotationTypes();
        AnnotatedType[] annotatedTypes = isShowTypeAnnotation ? clazz.getAnnotatedInterfaces() : null;
        String interfaces;
        if (manager.getConfigurationManager().isShowGenericSignatures()) {
            interfaces = String.join(", ", getMultipleParentTypes(clazz.getGenericInterfaces(), annotatedTypes));
        } else {
            interfaces = String.join(", ", getMultipleParentTypes(clazz.getInterfaces(), annotatedTypes));
        }

        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + ' ' : "";
    }

    private List<String> getMultipleParentTypes(Type[] parentTypes, AnnotatedType[] annotatedTypes) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (int index = 0; index < parentTypes.length; index++) {
            multipleParentTypes.add(getSingleParentType(parentTypes[index], ifEmpty(annotatedTypes, index)));
        }
        return multipleParentTypes;
    }

    private String getSingleParentType(Type parentType, AnnotatedType annotatedType) {
        return genericTypeParser.resolveType(parentType, annotatedType);
    }

    private AnnotatedType ifEmpty(AnnotatedType[] types, int index) {
        return types == null || types.length == 0 ? null : types[index];
    }
}