package com.kiselev.reflection.ui.impl.reflection.method;

import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.argument.ArgumentUtils;
import com.kiselev.reflection.ui.impl.reflection.exception.ExceptionUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.reflection.name.NameUtils;
import com.kiselev.reflection.ui.impl.reflection.value.ValueUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    public String getMethods(Class<?> clazz) {
        String methods = "";

        List<String> methodList = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            methodList.add(getMethod(method));
        }

        if (!methodList.isEmpty()) {
            methods += String.join("\n\n", methodList) + "\n";
        }

        return methods;
    }

    private String getMethod(Method method) {
        String methodSignature = "";

        String annotations = new AnnotationUtils().getAnnotations(method);

        String indent = new IndentUtils().getIndent(method);

        String isDefault = method.isDefault() ? "default " : "";

        String modifiers = isDefault + new ModifiersUtils().getModifiers(method.getModifiers());

        String generics = new GenericsUtils().getGenerics(method);

        String returnType = new GenericsUtils().resolveType(method.getGenericReturnType(), method.getAnnotatedReturnType());

        String methodName = new NameUtils().getMemberName(method);

        String arguments = new ArgumentUtils().getArguments(method);

        String defaultAnnotationValue = getDefaultAnnotationValue(method);

        String exceptions = new ExceptionUtils().getExceptions(method);

        String body = isMethodRealization(method) ? " {\n" + indent + "    /* Compiled code */" + "\n" + indent + "}" : ";";

        methodSignature += annotations + indent + modifiers + generics + returnType + " " + methodName + arguments + defaultAnnotationValue + exceptions + body;

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
}
