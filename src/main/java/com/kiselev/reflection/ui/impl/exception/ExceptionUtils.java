package com.kiselev.reflection.ui.impl.exception;

import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {

    public String getExceptions(Executable executable) {
        String exceptions = "";

        List<String> exceptionTypes = new ArrayList<>();

        for (Type type : executable.getGenericExceptionTypes()) {
            exceptionTypes.add(Class.class.cast(type).getSimpleName());
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
