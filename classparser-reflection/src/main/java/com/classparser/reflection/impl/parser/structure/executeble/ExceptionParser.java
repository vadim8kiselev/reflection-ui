package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionParser {

    public static String getExceptions(Executable executable) {
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
                exceptionType = GenericTypeParser.resolveType(genericExceptionType, annotatedExceptionType);
            } else {
                exceptionType = GenericTypeParser.resolveType(genericExceptionTypes[index], null);
            }
            exceptionTypes.add(exceptionType);
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
