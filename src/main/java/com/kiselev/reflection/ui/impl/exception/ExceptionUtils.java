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
        for (int index = 0; index < genericExceptionTypes.length; index++) {
            String exceptionType = new GenericsUtils().resolveType(genericExceptionTypes[index], annotatedExceptionTypes[index]);
            exceptionTypes.add(exceptionType);
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
