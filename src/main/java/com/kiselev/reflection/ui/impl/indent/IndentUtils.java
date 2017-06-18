package com.kiselev.reflection.ui.impl.indent;

import java.lang.reflect.Member;

public class IndentUtils {

    public String getIndent(Object object) {
        StringBuilder indent = new StringBuilder();

        Class<?> declaringClass;

        if (object instanceof Member) {

            Member member = Member.class.cast(object);
            declaringClass = member.getDeclaringClass();

            if (declaringClass != null) {
                indent.append("    ");
            }
        } else if (object instanceof Class) {
            declaringClass = Class.class.cast(object);
        } else {
            return "";
        }

        while ((declaringClass = declaringClass.getDeclaringClass()) != null) {
            indent.append("    ");
        }

        return indent.toString();
    }
}
