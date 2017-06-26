package com.kiselev.reflection.ui.impl.reflection.constructor;

import com.kiselev.reflection.ui.impl.reflection.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.reflection.argument.ArgumentUtils;
import com.kiselev.reflection.ui.impl.reflection.exception.ExceptionUtils;
import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.reflection.modifier.ModifiersUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorUtils {

    public String getConstructors(Class<?> clazz) {
        String constructors = "";

        List<String> constructorList = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            constructorList.add(getConstructor(constructor));
        }

        if (!constructorList.isEmpty()) {
            constructors += String.join("\n\n", constructorList) + "\n";
        }

        return constructors;
    }

    private String getConstructor(Constructor constructor) {
        String constructorSignature = "";

        String annotations = new AnnotationUtils().getAnnotations(constructor);

        String indent = new IndentUtils().getIndent(constructor);

        String modifiers = new ModifiersUtils().getModifiers(constructor.getModifiers());

        String generics = new GenericsUtils().getGenerics(constructor);

        String constructorName = new GenericsUtils().resolveType(constructor.getDeclaringClass(), constructor.getAnnotatedReturnType());

        String arguments = new ArgumentUtils().getArguments(constructor);

        String exceptions = new ExceptionUtils().getExceptions(constructor);

        String body = " {\n" + indent + "    /* Compiled code */" + "\n" + indent + "}";

        constructorSignature += annotations + indent + modifiers + generics + constructorName + arguments + exceptions + body;

        return constructorSignature;
    }
}
