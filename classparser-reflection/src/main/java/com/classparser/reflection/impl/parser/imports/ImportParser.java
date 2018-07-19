package com.classparser.reflection.impl.parser.imports;

import com.classparser.reflection.impl.configuration.ReflectionParserManager;
import com.classparser.reflection.impl.parser.structure.PackageParser;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ImportParser {

    private final ThreadLocal<Set<Class<?>>> threadLocalClassesForImport;

    private final ReflectionParserManager manager;

    public ImportParser(ReflectionParserManager manager) {
        this.manager = manager;
        this.threadLocalClassesForImport = new ThreadLocal<>();
        this.threadLocalClassesForImport.set(new HashSet<>());
    }

    private Set<Class<?>> getImportClasses() {
        Set<Class<?>> classes = threadLocalClassesForImport.get();
        if (classes == null) {
            classes = new HashSet<>();
            threadLocalClassesForImport.set(classes);
        }

        return classes;
    }

    public boolean addImport(Class<?> classForImport) {
        if (manager.getParsedClass() == null) {
            return false;
        }

        classForImport = resolveClass(classForImport);

        if (isNeedFullName(classForImport) || !manager.getConfigurationManager().isEnabledImports()) {
            return false;
        } else {
            getImportClasses().add(classForImport);
            return true;
        }
    }

    public String getImports() {
        Set<String> imports = new TreeSet<>();
        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        for (Class<?> className : getImportClasses()) {
            if (isAppendToImports(className)) {
                imports.add("import " + className.getName() + ';' + lineSeparator);
            }
        }

        clearState();
        return !imports.isEmpty() ? String.join("", imports) + lineSeparator : "";
    }

    private void clearState() {
        this.threadLocalClassesForImport.remove();
    }

    private boolean isNeedFullName(Class<?> classForImport) {
        Set<Class<?>> classes = getImportClasses();
        for (Class<?> clazz : classes) {
            if (areEqualBySimpleName(clazz, classForImport) && !areEqualByName(clazz, classForImport)) {
                return !classes.contains(classForImport);
            }
        }

        return false;
    }

    private boolean isAppendToImports(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !"java.lang".equals(PackageParser.getPackageName(clazz))
                && manager.getParsedClass().getPackage() != clazz.getPackage();
    }

    private boolean areEqualByName(Class<?> source, Class<?> target) {
        return source.getName().equals(target.getName());
    }

    private boolean areEqualBySimpleName(Class<?> source, Class<?> target) {
        return getSimpleName(source).equals(getSimpleName(target));
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

    private String getSimpleName(Class<?> clazz) {
        String typeName = clazz.getSimpleName();
        if (typeName.isEmpty()) {
            typeName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
        }
        return typeName;
    }
}
