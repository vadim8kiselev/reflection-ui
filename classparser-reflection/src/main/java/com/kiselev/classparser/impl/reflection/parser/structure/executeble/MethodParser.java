package com.kiselev.classparser.impl.reflection.parser.structure.executeble;

import com.kiselev.classparser.impl.reflection.parser.ClassNameParser;
import com.kiselev.classparser.impl.reflection.parser.base.AnnotationParser;
import com.kiselev.classparser.impl.reflection.parser.base.GenericTypeParser;
import com.kiselev.classparser.impl.reflection.parser.base.IndentParser;
import com.kiselev.classparser.impl.reflection.parser.base.ModifierParser;
import com.kiselev.classparser.impl.reflection.parser.base.ValueParser;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodParser {

    private AnnotationParser annotationParser = new AnnotationParser();

    private IndentParser indentParser = new IndentParser();

    private GenericTypeParser genericTypeParser = new GenericTypeParser();

    private ClassNameParser classNameParser = new ClassNameParser();

    private ArgumentParser argumentParser = new ArgumentParser();

    private ValueParser valueParser = new ValueParser();

    private ExceptionParser exceptionParser = new ExceptionParser();

    private ModifierParser modifierParser = new ModifierParser();

    public String getMethods(Class<?> clazz) {
        String methods = "";

        List<String> methodList = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            methodList.add(getMethod(method));
        }

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        if (!methodList.isEmpty()) {
            methods += String.join(lineSeparator + lineSeparator, methodList) + lineSeparator;
        }

        return methods;
    }

    private String getMethod(Method method) {
        String methodSignature = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        String annotations = annotationParser.getAnnotations(method);

        String indent = indentParser.getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + getModifiers(method);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericTypeParser.getGenerics(method) : "";

        Type type = isShowGeneric ? method.getGenericReturnType() : method.getReturnType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? method.getAnnotatedReturnType() : null;

        String returnType = genericTypeParser.resolveType(type, annotatedType);

        String methodName = classNameParser.getMemberName(method);

        String arguments = argumentParser.getArguments(method);

        String defaultAnnotationValue = valueParser.getValue(method);

        String exceptions = exceptionParser.getExceptions(method);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = "";
        if (isMethodRealization(method)) {
            body = " {" + lineSeparator + indent + oneIndent +
                    "/* Compiled code */" + lineSeparator + indent + "}";
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
            String bridgeModifier = StateManager.getConfiguration().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
