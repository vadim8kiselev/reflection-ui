package com.classparser.reflection.impl.parser.base;

import com.classparser.reflection.impl.configuration.ConfigurationManager;
import com.classparser.reflection.impl.constants.Cast;
import com.classparser.reflection.impl.configuration.ReflectionParserManager;

import java.lang.reflect.Member;

public class IndentParser {

    private final ReflectionParserManager manager;

    public IndentParser(ReflectionParserManager manager) {
        this.manager = manager;
    }

    public String getIndent(Object object) {
        StringBuilder indent = new StringBuilder();
        ConfigurationManager configurationManager = manager.getConfigurationManager();

        Class<?> declaringClass;

        if (object instanceof Member) {
            Member member = Cast.MEMBER.cast(object);
            declaringClass = member.getDeclaringClass();

            if (declaringClass != null) {
                indent.append(configurationManager.getIndentSpaces());
            }
        } else if (object instanceof Class) {
            declaringClass = Cast.CLASS.cast(object);
        } else {
            return "";
        }

        Class<?> parsedClass = manager.getParsedClass();
        if (declaringClass != null && !parsedClass.equals(declaringClass)) {
            while ((declaringClass = declaringClass.getDeclaringClass()) != null) {
                indent.append(configurationManager.getIndentSpaces());
            }
        }

        return indent.toString();
    }
}
