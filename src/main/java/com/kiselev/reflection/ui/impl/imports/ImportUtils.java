package com.kiselev.reflection.ui.impl.imports;

import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aleksei Makarov on 18.06.2017.
 */
public class ImportUtils {

    private Class<?> parsedClass;

    private Class<?> currentClass;

    private List<Class<?>> classesForImport;

    private NameUtils utils;

    ImportUtils(Class<?> clazz) {
        this.parsedClass = clazz;
        this.currentClass = clazz;
        this.classesForImport = new ArrayList<>();
        this.utils = new NameUtils();
    }

    public Class<?> getParsedClass() {
        return parsedClass;
    }

    public void setCurrentClass(Class<?> currentClass) {
        this.currentClass = currentClass;
    }

    public Class<?> getCurrentClass() {
        return this.currentClass;
    }

    public void popCurrentClass() {
        this.currentClass = this.currentClass.getDeclaringClass();
    }

    public boolean addImport(Class<?> classForImport) {
        if (parsedClass == null) {
            return false;
        }

        if (classForImport.isArray()) {
            classForImport = resolveArray(classForImport);
        }

        if (classesForImport.contains(classForImport)) {
            return true;
        }

        if (!classForImport.isPrimitive() &&
                !"java.lang".equals(getPackageName(classForImport))
                && !getPackageName(parsedClass).equals(getPackageName(classForImport))) {

            if (!isContainsImportBySimpleName(classForImport)) {
                classesForImport.add(classForImport);
            } else {
                return false;
            }
        }

        return true;
    }

    public String getImports() {
        List<String> imports = new ArrayList<>();

        for (Class<?> className : classesForImport) {
            imports.add("import " + className.getName().replace("$", ".") + ";");
        }

        Collections.sort(imports);
        if (!imports.isEmpty()) {
            imports.add("\n");
        }

        clear();
        return String.join("\n", imports);
    }

    private void clear() {
        this.parsedClass = null;
        this.classesForImport = new ArrayList<>();
    }

    private boolean isContainsImportBySimpleName(Class<?> classForImport) {
        for (Class<?> clazz : classesForImport) {
            if (utils.getSimpleName(clazz).equals(utils.getSimpleName(classForImport))) {
                return true;
            }
        }

        return false;
    }

    private Class<?> resolveArray(Class<?> clazz) {
        if (clazz.isArray()) {
            return resolveArray(clazz.getComponentType());
        } else {
            return clazz;
        }
    }

    private String getPackageName(Class<?> clazz) {
        return clazz.getPackage() == null ? "" : clazz.getPackage().getName();
    }
}
