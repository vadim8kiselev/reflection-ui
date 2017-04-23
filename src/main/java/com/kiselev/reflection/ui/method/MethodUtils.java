package com.kiselev.reflection.ui.method;

import com.kiselev.reflection.ui.annotation.AnnotationsUtils;
import com.kiselev.reflection.ui.argument.ArgumentUtils;
import com.kiselev.reflection.ui.exception.ExceptionUtils;
import com.kiselev.reflection.ui.generic.GenericsUtils;
import com.kiselev.reflection.ui.indent.IndentUtils;
import com.kiselev.reflection.ui.modifier.ModifiersUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    public String getMethods(Class<?> clazz) {
        String methods = "";

        List<String> methodList = new ArrayList<String>();
        for (Method method : clazz.getDeclaredMethods()) {
            boolean isImplementation = getRealization(clazz, method);
            methodList.add(getMethod(method, isImplementation));
        }

        if (!methodList.isEmpty()) {
            methods += String.join("\n\n", methodList) + "\n";
        }

        return methods;
    }

    private boolean getRealization(Class<?> clazz, Method method) {
        if (clazz.isInterface()) {
            return true;
        } else if (Modifier.isNative(method.getModifiers()) || Modifier.isAbstract(method.getModifiers())) {
            return true;
        }
        return false;
    }

    private String getMethod(Method method, boolean isImplementation) {
        String methodSignature = "";

        String annotations = new AnnotationsUtils().getAnnotations(method);

        String indent = new IndentUtils().getIndent(method);

        String modifiers = new ModifiersUtils().getModifiers(method.getModifiers());

        String generics = new GenericsUtils().getGenerics(method);

        String returnType = new GenericsUtils().resolveType(method.getGenericReturnType());

        String methodName = method.getName();

        String arguments = new ArgumentUtils().getArguments(method);

        String exceptions = new ExceptionUtils().getExceptions(method);

        String implementation = isImplementation ? ";" : " {\n" + indent + "}";

        methodSignature += annotations + indent + modifiers + generics + returnType + " " + methodName + arguments + exceptions + implementation;

        return methodSignature;
    }
}
