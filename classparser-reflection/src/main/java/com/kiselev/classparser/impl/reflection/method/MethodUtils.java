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

        String annotations = new AnnotationUtils().getAnnotations(method);

        String indent = new IndentUtils().getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + getModifiers(method);

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        String generics = isShowGeneric ? new GenericsUtils().getGenerics(method) : "";

        Type type = isShowGeneric ? method.getGenericReturnType() : method.getReturnType();

        boolean isShowTypeAnnotation = StateManager.getConfiguration().isShowAnnotationTypes();

        AnnotatedType annotatedType = isShowTypeAnnotation ? method.getAnnotatedReturnType() : null;

        String returnType = new GenericsUtils().resolveType(type, annotatedType);

        String methodName = new NameUtils().getMemberName(method);

        String arguments = new ArgumentUtils().getArguments(method);

        String defaultAnnotationValue = new ValueUtils().getValue(method);

        String exceptions = new ExceptionUtils().getExceptions(method);

        String oneIndent = StateManager.getConfiguration().getIndentSpaces();

        String body = "";
        if (isMethodRealization(method)) {
            body = " {" + lineSeparator + indent + oneIndent + "/* Compiled code */" + lineSeparator + indent + "}";
        }

        methodSignature += annotations + indent + modifiers + generics + returnType;

        methodSignature += " " + methodName + arguments + defaultAnnotationValue + exceptions + body;

        return methodSignature;
    }

    private boolean isMethodRealization(Method method) {
        return !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers());
    }

    private String getModifiers(Method method) {
        String modifiers = new ModifiersUtils().getModifiers(method.getModifiers());
        modifiers = modifiers.replace("transient ", "");

        if (modifiers.contains("volatile")) {
            String bridgeModifier = StateManager.getConfiguration().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
