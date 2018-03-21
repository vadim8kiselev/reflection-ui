package com.classparser.reflection.impl;

import com.classparser.api.ClassParser;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.ClassTypeParser;
import com.classparser.reflection.impl.parser.InheritanceParser;
import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.structure.ClassesParser;
import com.classparser.reflection.impl.parser.structure.FieldParser;
import com.classparser.reflection.impl.parser.structure.PackageParser;
import com.classparser.reflection.impl.parser.structure.executeble.ConstructorParser;
import com.classparser.reflection.impl.parser.structure.executeble.MethodParser;
import com.classparser.reflection.impl.state.StateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReflectionParser implements ClassParser {

    @Override
    public String parseClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class can't be a null!");
        }

        String parsedClass = "";

        StateManager.registerImportUtils(clazz);

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        StateManager.setCurrentClass(clazz);

        String packageName = PackageParser.getPackage(clazz);

        String indent = IndentParser.getIndent(clazz);

        String classSignature = getClassSignature(clazz);

        String classContent = getClassContent(clazz);

        String imports = "";
        if (clazz.equals(StateManager.getParsedClass()) && StateManager.getConfiguration().isEnabledImports()) {
            imports = StateManager.getImportUtils().getImports();
        }

        parsedClass += packageName + imports + classSignature;

        parsedClass += '{' + lineSeparator + lineSeparator + classContent + indent + '}';

        StateManager.popCurrentClass();

        return parsedClass;
    }

    private String getClassSignature(Class<?> clazz) {
        String classSignature = "";

        String annotations = AnnotationParser.getAnnotations(clazz);

        String indent = IndentParser.getIndent(clazz);

        String modifiers = ModifierParser.getModifiers(clazz.getModifiers());

        String type = ClassTypeParser.getType(clazz);

        String typeName = ClassNameParser.getTypeName(clazz);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? GenericTypeParser.getGenerics(clazz) : " ";

        String inheritances = InheritanceParser.getInheritances(clazz);

        classSignature += annotations + indent + modifiers + type + typeName + generics + inheritances;

        return classSignature;
    }

    private String getClassContent(Class<?> clazz) {
        String classContent = "";

        String fields = FieldParser.getFields(clazz);

        String constructors = ConstructorParser.getConstructors(clazz);

        String methods = MethodParser.getMethods(clazz);

        boolean isShowInnerClasses = StateManager.getConfiguration().isShowInnerClasses();

        String classes = isShowInnerClasses ? ClassesParser.getClasses(clazz) : "";

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
