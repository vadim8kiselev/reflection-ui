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
        String superClass = getSingleParentType(clazz.getGenericSuperclass());
        return (!superClass.isEmpty()) ? "extends " + superClass + " " : "";
    }

    private String getInterfaces(Class<?> clazz) {
        String interfaces = String.join(", ", getMultipleParentTypes(clazz.getGenericInterfaces()));
        String relationship = clazz.isInterface() ? "extends " : "implements ";
        return (!interfaces.isEmpty()) ? relationship + interfaces + " " : "";
    }

    private List<String> getMultipleParentTypes(Type[] parentTypes) {
        List<String> multipleParentTypes = new ArrayList<>();
        for (Type superType : parentTypes) {
            multipleParentTypes.add(getSingleParentType(superType));
        }
        return multipleParentTypes;
    }

    private String getSingleParentType(Type parentType) {
        return new GenericsUtils().resolveType(parentType);
    }
}
