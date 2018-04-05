package com.classparser.reflection.impl.parser.structure.executeble;

import com.classparser.reflection.impl.parser.base.GenericTypeParser;
import com.classparser.reflection.impl.state.ReflectionParserManager;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExceptionParser {

    private final GenericTypeParser genericTypeParser;

    private final ReflectionParserManager manager;

    public ExceptionParser(GenericTypeParser genericTypeParser, ReflectionParserManager manager) {
        this.genericTypeParser = genericTypeParser;
        this.manager = manager;
    }

    public String getExceptions(Executable executable) {
        String exceptions = "";

        List<String> exceptionTypes = new ArrayList<>();
        AnnotatedType[] annotatedExceptionTypes = executable.getAnnotatedExceptionTypes();

        boolean isShowGeneric = manager.getConfigurationManager().isShowGenericSignatures();

        Type[] genericExceptionTypes = isShowGeneric ? executable.getGenericExceptionTypes() : executable.getExceptionTypes();
        for (int index = 0; index < genericExceptionTypes.length; index++) {
            String exceptionType;
            if (manager.getConfigurationManager().isShowAnnotationTypes()) {
                Type genericExceptionType = genericExceptionTypes[index];
                AnnotatedType annotatedExceptionType = annotatedExceptionTypes[index];
                exceptionType = genericTypeParser.resolveType(genericExceptionType, annotatedExceptionType);
            } else {
                exceptionType = genericTypeParser.resolveType(genericExceptionTypes[index], null);
            }
            exceptionTypes.add(exceptionType);
        }

        if (!exceptionTypes.isEmpty()) {
            exceptions += " throws " + String.join(", ", exceptionTypes);
        }

        return exceptions;
    }
}
