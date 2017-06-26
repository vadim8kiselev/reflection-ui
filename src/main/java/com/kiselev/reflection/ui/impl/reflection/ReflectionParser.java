package com.kiselev.reflection.ui.impl.reflection;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.classes.ClassUtils;
import com.kiselev.reflection.ui.impl.reflection.constructor.ConstructorUtils;
import com.kiselev.reflection.ui.impl.reflection.field.FieldUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.imports.StateManager;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.inheritance.InheritancesUtils;
import com.kiselev.reflection.ui.impl.reflection.method.MethodUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.packages.PackageUtils;
import com.kiselev.reflection.ui.impl.reflection.type.TypeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionParser implements ReflectionUI {

    @Override
    public String parseClass(Class<?> clazz) {
        String parsedClass = "";

        StateManager.registerImportUtils(clazz);

        StateManager.setCurrentClass(clazz);

        String packageName = new PackageUtils().getPackage(clazz);

        String indent = new IndentUtils().getIndent(clazz);

        String classSignature = getClassSignature(clazz);

        String classContent = getClassContent(clazz);

        String imports = "";
        if (clazz.equals(StateManager.getParsedClass())) {
            imports = StateManager.getImportUtils().getImports();
        }

        parsedClass += packageName + imports + classSignature + "{\n\n" + classContent + indent + "}";

        StateManager.popCurrentClass();

        return parsedClass;
    }

    private String getClassSignature(Class<?> clazz) {
        String classSignature = "";

        String annotations = new AnnotationUtils().getAnnotations(clazz);

        String indent = new IndentUtils().getIndent(clazz);

        String modifiers = new ModifiersUtils().getModifiers(clazz.getModifiers());

        String type = new TypeUtils().getType(clazz);

        String typeName = new NameUtils().getTypeName(clazz);

        String generics = new GenericsUtils().getGenerics(clazz);

        String inheritances = new InheritancesUtils().getInheritances(clazz);

        classSignature += annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = new FieldUtils().getFields(clazz);

        String constructors = new ConstructorUtils().getConstructors(clazz);

        String methods = new MethodUtils().getMethods(clazz);

        String classes = new ClassUtils().getClasses(clazz);

        //fields + constructors + methods + classes;
        classContent += composeContent(Arrays.asList(fields, constructors, methods, classes));

        return classContent;
    }

    private String composeContent(List<String> contents) {
        List<String> nonEmptyContents = new ArrayList<>();

        for (String content : contents) {
            if (!content.isEmpty()) {
                nonEmptyContents.add(content);
            }
        }

        return String.join("\n", nonEmptyContents);
    }
}
