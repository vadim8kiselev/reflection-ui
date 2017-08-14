package com.kiselev.classparser.impl.reflection;

import com.kiselev.classparser.api.ClassParser;
import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.classes.ClassUtils;
import com.kiselev.classparser.impl.reflection.constructor.ConstructorUtils;
import com.kiselev.classparser.impl.reflection.field.FieldUtils;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.indent.IndentUtils;
import com.kiselev.classparser.impl.reflection.inheritance.InheritancesUtils;
import com.kiselev.classparser.impl.reflection.method.MethodUtils;
import com.kiselev.classparser.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.classparser.impl.reflection.name.NameUtils;
import com.kiselev.classparser.impl.reflection.packages.PackageUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;
import com.kiselev.classparser.impl.reflection.type.TypeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReflectionParser implements ClassParser {

    @Override
    public String parseClass(Class<?> clazz) {
        String parsedClass = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        StateManager.registerImportUtils(clazz);

        StateManager.setCurrentClass(clazz);

        String packageName = new PackageUtils().getPackage(clazz);

        String indent = new IndentUtils().getIndent(clazz);

        String classSignature = getClassSignature(clazz);

        String classContent = getClassContent(clazz);

        String imports = "";
        if (clazz.equals(StateManager.getParsedClass()) && StateManager.getConfiguration().isEnabledImports()) {
            imports = StateManager.getImportUtils().getImports();
        }

        parsedClass += packageName + imports + classSignature;

        parsedClass += "{" + lineSeparator + lineSeparator + classContent + indent + "}";

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

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? new GenericsUtils().getGenerics(clazz) : " ";

        String inheritances = new InheritancesUtils().getInheritances(clazz);

        classSignature += annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = new FieldUtils().getFields(clazz);

        String constructors = new ConstructorUtils().getConstructors(clazz);

        String methods = new MethodUtils().getMethods(clazz);

        String classes = StateManager.getConfiguration().isShowInnerClasses() ? new ClassUtils().getClasses(clazz) : "";

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

        return String.join(StateManager.getConfiguration().getLineSeparator(), nonEmptyContents);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        StateManager.setConfiguration(configuration);
    }
}
