package com.classparser.reflection.impl;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.ClassTypeParser;
import com.classparser.reflection.impl.parser.InheritanceParser;
import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.base.ValueParser;
import com.classparser.reflection.impl.parser.imports.ImportParser;
import com.classparser.reflection.impl.parser.structure.ClassesParser;
import com.classparser.reflection.impl.parser.structure.FieldParser;
import com.classparser.reflection.impl.parser.structure.PackageParser;
import com.classparser.reflection.impl.parser.structure.executeble.ArgumentParser;
import com.classparser.reflection.impl.parser.structure.executeble.ConstructorParser;
import com.classparser.reflection.impl.parser.structure.executeble.ExceptionParser;
import com.classparser.reflection.impl.parser.structure.executeble.MethodParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReflectionParser implements ClassParser {

    private final AnnotationParser annotationParser;

    private final GenericTypeParser genericTypeParser;

    private final IndentParser indentParser;

    private final ModifierParser modifierParser;

    private final ClassTypeParser classTypeParser;

    private final ImportParser importParser;

    private final ClassNameParser classNameParser;

    private final InheritanceParser inheritanceParser;

    private final PackageParser packageParser;

    private final FieldParser fieldParser;

    private final ClassesParser classesParser;

    private final ConstructorParser constructorParser;

    private final MethodParser methodParser;

    private final ReflectionParserManager reflectionParserManager;

    public ReflectionParser() {
        reflectionParserManager = new ReflectionParserManager();
        classTypeParser = new ClassTypeParser();
        indentParser = new IndentParser(reflectionParserManager);
        modifierParser = new ModifierParser(reflectionParserManager);
        importParser = new ImportParser(reflectionParserManager);
        classNameParser = new ClassNameParser(importParser, reflectionParserManager);
        genericTypeParser = new GenericTypeParser(classNameParser, reflectionParserManager);
        ValueParser valueParser = new ValueParser(genericTypeParser, reflectionParserManager);
        annotationParser = new AnnotationParser(indentParser, valueParser, genericTypeParser, reflectionParserManager);
        genericTypeParser.setAnnotationParser(annotationParser);
        inheritanceParser = new InheritanceParser(genericTypeParser, reflectionParserManager);
        classesParser = new ClassesParser(this, reflectionParserManager);
        packageParser = new PackageParser(annotationParser, reflectionParserManager);
        ArgumentParser argumentParser = new ArgumentParser(reflectionParserManager, genericTypeParser,
                modifierParser, annotationParser);
        ExceptionParser exceptionParser = new ExceptionParser(genericTypeParser, reflectionParserManager);
        constructorParser = new ConstructorParser(reflectionParserManager, genericTypeParser, modifierParser,
                annotationParser, argumentParser, indentParser, exceptionParser);
        methodParser = new MethodParser(reflectionParserManager, genericTypeParser, modifierParser,
                annotationParser, argumentParser, indentParser, exceptionParser, classNameParser, valueParser);
        fieldParser = new FieldParser(reflectionParserManager, annotationParser, indentParser,
                modifierParser, genericTypeParser, classNameParser, valueParser);
    }

    @Override
    public String parseClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class can't be a null!");
        }

        if (reflectionParserManager.getParsedClass() == null) {
            reflectionParserManager.setParsedClass(clazz);
        }

        reflectionParserManager.setCurrentParsedClass(clazz);

        String parsedClass = "";

        String lineSeparator = reflectionParserManager.getConfigurationManager().getLineSeparator();

        String packageName = packageParser.getPackage(clazz);

        String indent = indentParser.getIndent(clazz);

        String classSignature = getClassSignature(clazz);

        String classContent = getClassContent(clazz);

        String imports = "";
        if (clazz.equals(reflectionParserManager.getParsedClass())) {
            if (reflectionParserManager.getConfigurationManager().isEnabledImports()) {
                imports = importParser.getImports();
            }
            reflectionParserManager.clearState();
        }

        parsedClass += packageName + imports + classSignature;

        parsedClass += '{' + lineSeparator + lineSeparator + classContent + indent + '}';

        reflectionParserManager.popCurrentClass();

        return parsedClass;
    }

    private String getClassSignature(Class<?> clazz) {
        String classSignature = "";

        String annotations = annotationParser.getAnnotations(clazz);

        String indent = indentParser.getIndent(clazz);

        String modifiers = modifierParser.getModifiers(clazz.getModifiers());

        String type = classTypeParser.getType(clazz);

        String typeName = classNameParser.getTypeName(clazz);

        boolean isShowGeneric = reflectionParserManager.getConfigurationManager().isShowGenericSignatures();

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

        boolean isShowInnerClasses = reflectionParserManager.getConfigurationManager().isShowInnerClasses();

        String classes = isShowInnerClasses ? classesParser.getClasses(clazz) : "";

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

        return String.join(reflectionParserManager.getConfigurationManager().getLineSeparator(), nonEmptyContents);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        reflectionParserManager.getConfigurationManager().reloadConfiguration(configuration);
    }
}
