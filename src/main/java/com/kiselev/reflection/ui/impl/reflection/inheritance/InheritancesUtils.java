package com.kiselev.reflection.ui.impl.reflection.inheritance;

import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;

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
        String superClass = getSingleParentType(clazz.getGenericSuperclass(), clazz.getAnnotatedSuperclass());
        return (!superClass.isEmpty()) ? "extends " + superClass + " " : "";
    }

    private String getInterfaces(Class<?> clazz) {
        String interfaces = String.join(", ", getMultipleParentTypes(clazz.getGenericInterfaces(), clazz.getAnnotatedInterfaces()));
        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + " " : "";
    }

    private List<String> getMultipleParentTypes(Type[] parentTypes, AnnotatedType[] annotatedTypes) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (int index = 0; index < parentTypes.length; index++) {
            multipleParentTypes.add(getSingleParentType(parentTypes[index], annotatedTypes[index]));
        }
        return multipleParentTypes;
    }

    private String getSingleParentType(Type parentType, AnnotatedType annotatedType) {
        return new GenericsUtils().resolveType(parentType, annotatedType);
    }
}
