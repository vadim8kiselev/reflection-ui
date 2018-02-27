package com.kiselev.classparser.impl.reflection.method;

import com.kiselev.classparser.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.classparser.impl.reflection.argument.ArgumentUtils;
import com.kiselev.classparser.impl.reflection.exception.ExceptionUtils;
import com.kiselev.classparser.impl.reflection.generic.GenericsUtils;
import com.kiselev.classparser.impl.reflection.indent.IndentUtils;
import com.kiselev.classparser.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.classparser.impl.reflection.name.NameUtils;
import com.kiselev.classparser.impl.reflection.state.StateManager;
import com.kiselev.classparser.impl.reflection.value.ValueUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    private AnnotationUtils annotationUtils = new AnnotationUtils();

    private IndentUtils indentUtils = new IndentUtils();

    private GenericsUtils genericsUtils = new GenericsUtils();

    private NameUtils nameUtils = new NameUtils();

    private ArgumentUtils argumentUtils = new ArgumentUtils();

    private ValueUtils valueUtils = new ValueUtils();

    private ExceptionUtils exceptionUtils = new ExceptionUtils();

    private ModifiersUtils modifiersUtils = new ModifiersUtils();

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

        String annotations = annotationUtils.getAnnotations(method);

        String indent = indentUtils.getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + getModifiers(method);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? genericsUtils.getGenerics(method) : "";

        Type type = isShowGeneric ? method.getGenericReturnType() : method.getReturnType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? method.getAnnotatedReturnType() : null;

        String returnType = genericsUtils.resolveType(type, annotatedType);

        String methodName = nameUtils.getMemberName(method);

        String arguments = argumentUtils.getArguments(method);

        String defaultAnnotationValue = valueUtils.getValue(method);

        String exceptions = exceptionUtils.getExceptions(method);

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
        String modifiers = modifiersUtils.getModifiers(method.getModifiers());
        modifiers = modifiers.replace("transient ", "");

        if (modifiers.contains("volatile")) {
            String bridgeModifier = StateManager.getConfiguration().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
