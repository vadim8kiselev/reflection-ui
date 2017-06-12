package com.kiselev.reflection.ui.impl.indent;

import java.lang.reflect.Member;

public class IndentUtils {

    public String getIndent(Object object) {
        String indent = "";

        Class<?> declaringClass;

        if (object instanceof Member) {

            Member member = Member.class.cast(object);
            declaringClass = member.getDeclaringClass();

            if (declaringClass != null) {
                indent += "    ";
            }
        } else if (object instanceof Class){
            declaringClass = Class.class.cast(object);
        } else {
            return "";
        }

        while ((declaringClass = declaringClass.getDeclaringClass()) != null) {
            indent += "    ";
        }

        return indent;
    }
}
