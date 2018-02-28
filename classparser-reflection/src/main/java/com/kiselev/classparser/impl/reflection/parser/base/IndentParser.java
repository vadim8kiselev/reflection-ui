package com.kiselev.classparser.impl.reflection.parser.base;

import com.kiselev.classparser.impl.reflection.constants.Cast;
import com.kiselev.classparser.impl.reflection.state.StateManager;

import java.lang.reflect.Member;

public class IndentParser {

    public String getIndent(Object object) {
        StringBuilder indent = new StringBuilder();

        Class<?> declaringClass;

        if (object instanceof Member) {
            Member member = Cast.MEMBER.cast(object);
            declaringClass = member.getDeclaringClass();

            if (declaringClass != null) {
                indent.append(StateManager.getConfiguration().getIndentSpaces());
            }
        } else if (object instanceof Class) {
            declaringClass = Cast.CLASS.cast(object);
        } else {
            return "";
        }

        Class<?> parsedClass = StateManager.getParsedClass();
        if (declaringClass != null && !parsedClass.equals(declaringClass)) {
            while ((declaringClass = declaringClass.getDeclaringClass()) != null) {
                indent.append(StateManager.getConfiguration().getIndentSpaces());
            }
        }

        return indent.toString();
    }
}
