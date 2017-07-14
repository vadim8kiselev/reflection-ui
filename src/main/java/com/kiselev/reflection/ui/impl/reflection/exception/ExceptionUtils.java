package com.kiselev.reflection.ui.impl.reflection.exception;

import com.kiselev.reflection.ui.impl.reflection.generic.GenericsUtils;
import com.kiselev.reflection.ui.impl.reflection.state.StateManager;

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
        Type[] genericExceptionTypes = StateManager.getConfiguration().isShowGenericSignatures() ? executable.getGenericExceptionTypes() : executable.getExceptionTypes();
        for (int index = 0; index < genericExceptionTypes.length; index++) {
            String exceptionType;
            if (StateManager.getConfiguration().isShowAnnotationTypes()) {
                exceptionType = new GenericsUtils().resolveType(genericExceptionTypes[index], annotatedExceptionTypes[index]);
            } else {
                exceptionType = new GenericsUtils().resolveType(genericExceptionTypes[index], null);
            }
            exceptionTypes.add(exceptionType);
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
