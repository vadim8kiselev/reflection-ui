package com.kiselev.reflection.ui.impl.inheritance;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

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
        String superClass = getSingleParentType(clazz.getGenericSuperclass(), clazz);
        return (!superClass.isEmpty()) ? "extends " + superClass + " " : "";
    }

    private String getInterfaces(Class<?> clazz) {
        String interfaces = String.join(", ", getMultipleParentTypes(clazz.getGenericInterfaces(), clazz));
        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + " " : "";
    }

    private List<String> getMultipleParentTypes(Type[] parentTypes, Class<?> parsedClass) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (Type superType : parentTypes) {
            multipleParentTypes.add(getSingleParentType(superType, parsedClass));
        }
        return multipleParentTypes;
    }

    private String getSingleParentType(Type parentType, Class<?> parsedClass) {
        return new GenericsUtils().resolveType(parentType, parsedClass);
    }
}
