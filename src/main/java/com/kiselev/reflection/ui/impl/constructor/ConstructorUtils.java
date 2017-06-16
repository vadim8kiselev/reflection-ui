package com.kiselev.reflection.ui.impl.constructor;

import com.kiselev.reflection.ui.impl.annotation.AnnotationUtils;
import com.kiselev.reflection.ui.impl.argument.ArgumentUtils;
import com.kiselev.reflection.ui.impl.exception.ExceptionUtils;
import com.kiselev.reflection.ui.impl.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.indent.IndentUtils;
import com.kiselev.reflection.ui.impl.modifier.ModifiersUtils;
import com.kiselev.reflection.ui.impl.name.NameUtils;

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

        String constructorName = new NameUtils().getTypeName(constructor.getDeclaringClass());

        String arguments = new ArgumentUtils().getArguments(constructor);

        String exceptions = new ExceptionUtils().getExceptions(constructor);

        constructorSignature += annotations + indent + modifiers + generics + constructorName + arguments + exceptions + " {\n" + indent + "}";

        return constructorSignature;
    }
}
