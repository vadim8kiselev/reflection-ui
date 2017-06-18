package com.kiselev.reflection.ui.impl.exception;

import com.kiselev.reflection.ui.impl.generic.GenericsUtils;

import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {

    public String getExceptions(Executable executable) {
        String exceptions = "";

        List<String> exceptionTypes = new ArrayList<>();

        for (Type type : executable.getGenericExceptionTypes()) {
            exceptionTypes.add(new GenericsUtils().resolveType(type, executable.getDeclaringClass()));
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
