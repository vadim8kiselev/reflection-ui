package com.kiselev.reflection.ui.impl.imports;

import com.kiselev.reflection.ui.impl.name.NameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksei Makarov on 18.06.2017.
 */
public class ImportUtils {

    private Class<?> parsedClass;

    private List<Class<?>> classesForImport;

    private NameUtils utils;

    ImportUtils(Class<?> clazz) {
        this.parsedClass = clazz;
        this.classesForImport = new ArrayList<>();
        this.utils = new NameUtils();
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
                !"java.lang".contains(getPackageName(classForImport))
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
        StringBuilder builder = new StringBuilder();

        for (Class<?> className : classesForImport) {
            builder.append("import ").append(className.getName()).append(";\n");
        }

        if (builder.length() != 0) {
            builder.append("\n");
        }

        return builder.toString();
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
        return clazz.getPackage().getName();
    }
}
