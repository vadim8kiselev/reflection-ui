package com.kiselev.reflection.ui.impl.exception;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {

    public String getExceptions(Executable executable) {
        String exceptions = "";

        List<String> exceptionTypes = new ArrayList<>();
        AnnotatedType[] annotatedExceptionTypes = executable.getAnnotatedExceptionTypes();
        Type[] genericExceptionTypes = executable.getGenericExceptionTypes();
        for (int i = 0; i < genericExceptionTypes.length; i++) {
            exceptionTypes.add(new GenericsUtils().resolveType(genericExceptionTypes[i], annotatedExceptionTypes[i]));
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
