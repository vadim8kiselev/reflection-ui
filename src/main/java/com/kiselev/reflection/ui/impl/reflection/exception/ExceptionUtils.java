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

        boolean isShowGeneric = StateManager.getConfiguration().isShowGenericSignatures();

        Type[] genericExceptionTypes = isShowGeneric ? executable.getGenericExceptionTypes() : executable.getExceptionTypes();
        for (int index = 0; index < genericExceptionTypes.length; index++) {
            String exceptionType;
            if (StateManager.getConfiguration().isShowAnnotationTypes()) {
                Type genericExceptionType = genericExceptionTypes[index];
                AnnotatedType annotatedExceptionType = annotatedExceptionTypes[index];
                exceptionType = new GenericsUtils().resolveType(genericExceptionType, annotatedExceptionType);
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
