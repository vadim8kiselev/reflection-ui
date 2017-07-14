package com.kiselev.reflection.ui.impl.reflection.method;

import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.argument.ArgumentUtils;
import com.kiselev.reflection.ui.impl.reflection.exception.ExceptionUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;
import com.kiselev.reflection.ui.impl.reflection.value.ValueUtils;

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

        String generics = StateManager.getConfiguration().isShowGenericSignatures() ? new GenericsUtils().getGenerics(method) : "";

        Type type = StateManager.getConfiguration().isShowGenericSignatures() ? method.getGenericReturnType() : method.getReturnType();

        AnnotatedType annotatedType = StateManager.getConfiguration().isShowAnnotationTypes() ? method.getAnnotatedReturnType() : null;

        String returnType = new GenericsUtils().resolveType(type, annotatedType);

        String methodName = new NameUtils().getMemberName(method);

        String arguments = new ArgumentUtils().getArguments(method);

        String defaultAnnotationValue = getDefaultAnnotationValue(method);

        String exceptions = new ExceptionUtils().getExceptions(method);

        String body = isMethodRealization(method) ? " {" + lineSeparator + indent +
                StateManager.getConfiguration().getIndentSpaces() + "/* Compiled code */" + lineSeparator + indent + "}" : ";";

        methodSignature += annotations + indent + modifiers + generics + returnType
                + " " + methodName + arguments + defaultAnnotationValue + exceptions + body;

        return methodSignature;
    }

    // TODO : Move to *Utils
    private String getDefaultAnnotationValue(Method method) {
        String defaultAnnotationValue = "";

        if (method.getDeclaringClass().isAnnotation()) {
            String defaultValue = new ValueUtils().getValue(method.getDefaultValue());

            if (defaultValue != null) {
                defaultAnnotationValue += " default " + defaultValue;
            }
        }

        return defaultAnnotationValue;
    }

    private boolean isMethodRealization(Method method) {
        return !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers());
    }

    private String getModifiers(Method method) {
        String modifiers = new ModifiersUtils().getModifiers(method.getModifiers()).replace("transient ", "");
        if (modifiers.contains("volatile")) {
            String bridgeModifier = StateManager.getConfiguration().isShowNonJavaModifiers() ? "bridge " : "";
            modifiers = bridgeModifier + modifiers.replace("volatile ", "");
        }

        return modifiers;
    }
}
