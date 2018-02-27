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

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private IndentUtils indentUtils = new IndentUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private NameUtils nameUtils = new NameUtils();

    private ModifiersUtils modifiersUtils = new ModifiersUtils();

    private PackageUtils packageUtils = new PackageUtils();

    private TypeUtils typeUtils = new TypeUtils();

    private InheritancesUtils inheritancesUtils = new InheritancesUtils();

    private FieldUtils fieldUtils = new FieldUtils();

    private MethodUtils methodUtils = new MethodUtils();

    private ConstructorUtils constructorUtils = new ConstructorUtils();

    private ClassUtils classUtils = new ClassUtils();

    @Override
    public String parseClass(Class<?> clazz) {
        String parsedClass = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        StateManager.registerImportUtils(clazz);

        StateManager.setCurrentClass(clazz);

        String packageName = packageUtils.getPackage(clazz);

        String indent = indentUtils.getIndent(clazz);

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

        String annotations = annotationUtils.getAnnotations(clazz);

        String indent = indentUtils.getIndent(clazz);

        String modifiers = modifiersUtils.getModifiers(clazz.getModifiers());

        String type = typeUtils.getType(clazz);

        String typeName = nameUtils.getTypeName(clazz);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericsUtils.getGenerics(clazz) : " ";

        String inheritances = inheritancesUtils.getInheritances(clazz);

        classSignature += annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = fieldUtils.getFields(clazz);

        String constructors = constructorUtils.getConstructors(clazz);

        String methods = methodUtils.getMethods(clazz);

        boolean isShowInnerClasses = StateManager.getConfiguration().isShowInnerClasses();

        String classes = isShowInnerClasses ? classUtils.getClasses(clazz) : "";

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
