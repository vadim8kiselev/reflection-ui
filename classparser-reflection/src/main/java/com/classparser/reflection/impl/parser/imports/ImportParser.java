package com.classparser.reflection.impl.parser.imports;

import com.classparser.reflection.impl.parser.structure.PackageParser;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.state.StateManager;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ImportParser {

    private Set<Class<?>> classesForImport;

    public ImportParser() {
        this.classesForImport = new HashSet<>();
    }

    public boolean addImport(Class<?> classForImport) {
        if (StateManager.getParsedClass() == null) {
            return false;
        }

        classForImport = resolveClass(classForImport);

        if (isNeedFullName(classForImport) || !StateManager.getConfiguration().isEnabledImports()) {
            return false;
        } else {
            classesForImport.add(classForImport);
            return true;
        }
    }

    public String getImports() {
        Set<String> imports = new TreeSet<>();
        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        for (Class<?> className : classesForImport) {
            if (isAppendToImports(className)) {
                imports.add("import " + className.getName() + ";" + lineSeparator);
            }
        }

        clearState();
        return !imports.isEmpty() ? String.join("", imports) + lineSeparator : "";
    }

    private void clearState() {
        StateManager.clearState();
        this.classesForImport.clear();
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
                && !"java.lang".equals(new PackageParser().getPackageName(clazz))
                && StateManager.getParsedClass().getPackage() != clazz.getPackage();
    }

    private boolean areEqualByName(Class<?> source, Class<?> target) {
        return source.getName().equals(target.getName());
    }

    private boolean areEqualBySimpleName(Class<?> source, Class<?> target) {
        ClassNameParser classNameParser = new ClassNameParser();
        return classNameParser.getSimpleName(source).equals(classNameParser.getSimpleName(target));
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
}
