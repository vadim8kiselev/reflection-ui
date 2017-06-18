package com.kiselev.reflection.ui.impl.inheritance;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InheritancesUtils {

    public String getInheritances(Class<?> clazz) {
        String inheritances = "";

        inheritances += getSuperClass(clazz);

        inheritances += getInterfaces(clazz);

        return inheritances;
    }

    private String getSuperClass(Class<?> clazz) {
        String superClass = getSingleParentType(clazz.getGenericSuperclass(), clazz.getAnnotatedSuperclass(), clazz);
        return (!superClass.isEmpty()) ? "extends " + superClass + " " : "";
    }

    private String getInterfaces(Class<?> clazz) {
        String interfaces = String.join(", ", getMultipleParentTypes(clazz.getGenericInterfaces(), clazz.getAnnotatedInterfaces(), clazz));
        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + " " : "";
    }

    private List<String> getMultipleParentTypes(Type[] parentTypes, AnnotatedType[] annotatedTypes, Class<?> parsedClass) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (int i = 0; i < parentTypes.length; i++) {
            multipleParentTypes.add(getSingleParentType(parentTypes[i], annotatedTypes[i], parsedClass));
        }

        return multipleParentTypes;
    }

    private String getSingleParentType(Type parentType, AnnotatedType annotatedType, Class<?> parsedClass) {
        return new GenericsUtils().resolveType(parentType, annotatedType, parsedClass);
    }
}
