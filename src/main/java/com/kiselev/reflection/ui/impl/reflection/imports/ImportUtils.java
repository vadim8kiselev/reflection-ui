package com.kiselev.reflection.ui.impl.reflection.imports;

import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by Aleksei Makarov on 06/18/2017.
 */
public class ImportUtils {

    private Set<Class<?>> classesForImport;

    private Set<Class<?>> allClasses;

    ImportUtils() {
        this.classesForImport = new HashSet<>();
        this.allClasses = new HashSet<>();
    }

    public boolean addImport(Class<?> classForImport) {
        if (StateManager.getParsedClass() == null) {
            return false;
        }

        if (classForImport.isMemberClass()) {
            classForImport = resolveMemberClass(classForImport);
        }

        if (classForImport.isArray()) {
            classForImport = resolveArray(classForImport);
        }

        if (isNeedFullName(classForImport)) {
            return false;
        } else if (!classesForImport.contains(classForImport)
                && !classForImport.isPrimitive()
                && !"java.lang".equals(getPackageName(classForImport))
                && StateManager.getParsedClass().getPackage() != classForImport.getPackage()) {

            classesForImport.add(classForImport);
        }

        allClasses.add(classForImport);
        return true;
    }

    public String getImports() {
        List<String> imports = new ArrayList<>();

        for (Class<?> className : classesForImport) {
            imports.add("import " + className.getName() + ";\n");
        }

        Collections.sort(imports); // TODO : Code style

        clearState();
        return !imports.isEmpty() ? String.join("", imports) + "\n" : "";
    }

    private void clearState() {
        StateManager.clearState();
        this.classesForImport = new HashSet<>();
    }

    private boolean isNeedFullName(Class<?> classForImport) {
        return isAllContainsBySimpleName(allClasses, classForImport) && !allClasses.contains(classForImport);
    }

    private boolean isAllContainsBySimpleName(Set<Class<?>> classes, Class<?> classForImport) {
        for (Class<?> clazz : classes) {
            if (areEqualBySimpleName(clazz, classForImport) && !areEqualByName(clazz, classForImport)) {
                return true;
            }
        }

        return false;
    }

    private boolean areEqualByName(Class<?> source, Class<?> target) {
        return source.getName().equals(target.getName());
    }

    private boolean areEqualBySimpleName(Class<?> source, Class<?> target) {
        NameUtils nameUtils = new NameUtils();
        return nameUtils.getSimpleName(source).equals(nameUtils.getSimpleName(target));
    }

    private Class<?> resolveArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return resolveArray(clazz.getComponentType());
        } else {
            return clazz;
        }
    }

    private Class<?> resolveMemberClass(Class<?> clazz) {
        if (clazz.isMemberClass()) {
            return resolveMemberClass(clazz.getEnclosingClass());
        } else {
            return clazz;
        }
    }

    private String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }

    public boolean isCleared() {
        return StateManager.getParsedClass() == null;
    }
}
