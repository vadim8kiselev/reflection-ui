package com.classparser.reflection.impl;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.parser.ClassTypeParser;
import com.classparser.reflection.impl.parser.InheritanceParser;
import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.structure.FieldParser;
import com.classparser.reflection.impl.parser.structure.PackageParser;
import com.classparser.reflection.impl.parser.structure.executeble.ConstructorParser;
import com.classparser.reflection.impl.state.StateManager;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.structure.ClassesParser;
import com.classparser.reflection.impl.parser.structure.executeble.MethodParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReflectionParser implements ClassParser {

    private AnnotationParser annotationParser = new AnnotationParser();

    private IndentParser indentParser = new IndentParser();

    private GenericTypeParser genericTypeParser = new GenericTypeParser();

    private ClassNameParser classNameParser = new ClassNameParser();

    private ModifierParser modifierParser = new ModifierParser();

    private PackageParser packageParser = new PackageParser();

    private ClassTypeParser classTypeParser = new ClassTypeParser();

    private InheritanceParser inheritanceParser = new InheritanceParser();

    private FieldParser fieldParser = new FieldParser();

    private MethodParser methodParser = new MethodParser();

    private ConstructorParser constructorParser = new ConstructorParser();

    private ClassesParser classesParser = new ClassesParser();

    @Override
    public String parseClass(Class<?> clazz) {
        String parsedClass = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        StateManager.registerImportUtils(clazz);

        StateManager.setCurrentClass(clazz);

        String packageName = packageParser.getPackage(clazz);

        String indent = indentParser.getIndent(clazz);

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

        String annotations = annotationParser.getAnnotations(clazz);

        String indent = indentParser.getIndent(clazz);

        String modifiers = modifierParser.getModifiers(clazz.getModifiers());

        String type = classTypeParser.getType(clazz);

        String typeName = classNameParser.getTypeName(clazz);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericTypeParser.getGenerics(clazz) : " ";

        String inheritances = inheritanceParser.getInheritances(clazz);

        classSignature += annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = fieldParser.getFields(clazz);

        String constructors = constructorParser.getConstructors(clazz);

        String methods = methodParser.getMethods(clazz);

        boolean isShowInnerClasses = StateManager.getConfiguration().isShowInnerClasses();

        String classes = isShowInnerClasses ? classesParser.getClasses(clazz) : "";

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
