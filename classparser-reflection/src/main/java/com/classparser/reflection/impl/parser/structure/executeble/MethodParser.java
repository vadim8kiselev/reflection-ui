package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.configuration.ConfigurationManager;
import com.classparser.reflection.impl.parser.ClassNameParser;
import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.base.ValueParser;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodParser {

    private final ReflectionParserManager manager;

    private final GenericTypeParser genericTypeParser;

    private final ModifierParser modifierParser;

    private final AnnotationParser annotationParser;

    private final ArgumentParser argumentParser;

    private final IndentParser indentParser;

    private final ExceptionParser exceptionParser;

    private final ClassNameParser classNameParser;

    private final ValueParser valueParser;

    public MethodParser(ReflectionParserManager manager, GenericTypeParser genericTypeParser,
                        ModifierParser modifierParser, AnnotationParser annotationParser,
                        ArgumentParser argumentParser, IndentParser indentParser,
                        ExceptionParser exceptionParser, ClassNameParser classNameParser, ValueParser valueParser) {
        this.manager = manager;
        this.genericTypeParser = genericTypeParser;
        this.modifierParser = modifierParser;
        this.annotationParser = annotationParser;
        this.argumentParser = argumentParser;
        this.indentParser = indentParser;
        this.exceptionParser = exceptionParser;
        this.classNameParser = classNameParser;
        this.valueParser = valueParser;
    }

    public String getMethods(Class<?> clazz) {
        String methods = "";

        List<String> methodList = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            methodList.add(getMethod(method));
        }

        String lineSeparator = manager.getConfigurationManager().getLineSeparator();

        if (!methodList.isEmpty()) {
            methods += String.join(lineSeparator + lineSeparator, methodList) + lineSeparator;
        }

        return methods;
    }

    private String getMethod(Method method) {
        String methodSignature = "";

        ConfigurationManager configurationManager = manager.getConfigurationManager();

        String lineSeparator = configurationManager.getLineSeparator();

        String annotations = annotationParser.getAnnotations(method);

        String indent = indentParser.getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + getModifiers(method);

        boolean isShowGeneric = configurationManager.isShowGenericSignatures();

        String generics = isShowGeneric ? genericTypeParser.getGenerics(method) : "";

        Type type = isShowGeneric ? method.getGenericReturnType() : method.getReturnType();

        boolean isShowTypeAnnotation = configurationManager.isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? method.getAnnotatedReturnType() : null;

        String returnType = genericTypeParser.resolveType(type, annotatedType);

        String methodName = classNameParser.getMemberName(method);

        String arguments = argumentParser.getArguments(method);

        String defaultAnnotationValue = valueParser.getValue(method);

        String exceptions = exceptionParser.getExceptions(method);

        String oneIndent = configurationManager.getIndentSpaces();

        String body = ";";
        if (isMethodRealization(method)) {
            body = " {" + lineSeparator + indent + oneIndent +
                    "/* Compiled code */" + lineSeparator + indent + '}';
        }

        methodSignature += annotations + indent + modifiers + generics + returnType;

        methodSignature += " " + methodName + arguments + defaultAnnotationValue + exceptions + body;

        return methodSignature;
    }

    private boolean isMethodRealization(Method method) {
        return !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers());
    }

    private String getModifiers(Method method) {
        String modifiers = modifierParser.getModifiers(method.getModifiers());
        modifiers = modifiers.replace("transient ", "");

        if (modifiers.contains("volatile")) {
            String bridgeModifier = manager.getConfigurationManager().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
