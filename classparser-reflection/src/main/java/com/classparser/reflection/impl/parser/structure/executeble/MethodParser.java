package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.AnnotationParser;
import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.parser.base.IndentParser;
import com.classparser.reflection.impl.parser.base.ModifierParser;
import com.classparser.reflection.impl.parser.base.ValueParser;
import com.classparser.reflection.impl.state.StateManager;
import com.classparser.reflection.impl.parser.ClassNameParser;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodParser {

    public static String getMethods(Class<?> clazz) {
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

    private static String getMethod(Method method) {
        String methodSignature = "";

        String lineSeparator = StateManager.getConfiguration().getLineSeparator();

        String annotations = AnnotationParser.getAnnotations(method);

        String indent = IndentParser.getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + getModifiers(method);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? GenericTypeParser.getGenerics(method) : "";

        Type type = isShowGeneric ? method.getGenericReturnType() : method.getReturnType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? method.getAnnotatedReturnType() : null;

        String returnType = GenericTypeParser.resolveType(type, annotatedType);

        String methodName = ClassNameParser.getMemberName(method);

        String arguments = ArgumentParser.getArguments(method);

        String defaultAnnotationValue = ValueParser.getValue(method);

        String exceptions = ExceptionParser.getExceptions(method);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = ";";
        if (isMethodRealization(method)) {
            body = " {" + lineSeparator + indent + oneIndent +
                    "/* Compiled code */" + lineSeparator + indent + "}";
        }

        methodSignature += annotations + indent + modifiers + generics + returnType;

        methodSignature += " " + methodName + arguments + defaultAnnotationValue + exceptions + body;

        return methodSignature;
    }

    private static boolean isMethodRealization(Method method) {
        return !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers());
    }

    private static String getModifiers(Method method) {
        String modifiers = ModifierParser.getModifiers(method.getModifiers());
        modifiers = modifiers.replace("transient ", "");

        if (modifiers.contains("volatile")) {
            String bridgeModifier = StateManager.getConfiguration().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
