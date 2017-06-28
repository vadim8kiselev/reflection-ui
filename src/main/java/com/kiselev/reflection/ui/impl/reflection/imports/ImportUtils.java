package com.kiselev.reflection.ui.impl.reflection.imports;

import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;

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

    public ImportUtils() {
        this.classesForImport = new HashSet<>();
    }

    public boolean addImport(Class<?> classForImport) {
        if (StateManager.getParsedClass() == null) {
            return false;
        }

        classForImport = resolveClass(classForImport);

        if (isNeedFullName(classForImport)) {
            return false;
        } else {
            classesForImport.add(classForImport);
        }

        return true;
    }

    public String getImports() {
        List<String> imports = new ArrayList<>();

        for (Class<?> className : classesForImport) {
            if (isAppendToImports(className)) {
                imports.add("import " + className.getName() + ";\n");
            }
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
        for (Class<?> clazz : classesForImport) {
            if (areEqualBySimpleName(clazz, classForImport) && !areEqualByName(clazz, classForImport)) {
                return !classesForImport.contains(classForImport);
            }
        }

        return false;
    }

    private boolean isAppendToImports(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !"java.lang".equals(getPackageName(clazz))
                && StateManager.getParsedClass().getPackage() != clazz.getPackage();
    }

    private boolean areEqualByName(Class<?> source, Class<?> target) {
        return source.getName().equals(target.getName());
    }

    private boolean areEqualBySimpleName(Class<?> source, Class<?> target) {
        NameUtils nameUtils = new NameUtils();
        return nameUtils.getSimpleName(source).equals(nameUtils.getSimpleName(target));
    }

    private Class<?> resolveClass(Class<?> clazz) {
        if (clazz.isArray()) {
            clazz = resolveArray(clazz);
        }

        if (clazz.isMemberClass()) {
            clazz = resolveMemberClass(clazz);
        }

        return clazz;
    }

    private Class<?> resolveArray(Class<?> clazz) {
        return clazz.isArray() ? resolveArray(clazz.getComponentType()) : clazz;
    }

    private Class<?> resolveMemberClass(Class<?> clazz) {
        return clazz.isMemberClass() ? resolveMemberClass(clazz.getEnclosingClass()) : clazz;
    }

    private String getPackageName(Class<?> clazz) {
        return clazz.getPackage() != null ? clazz.getPackage().getName() : "";
    }
}
