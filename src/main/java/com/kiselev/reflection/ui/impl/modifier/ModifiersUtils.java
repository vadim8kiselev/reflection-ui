package com.kiselev.reflection.ui.impl.modifier;

import java.lang.reflect.Modifier;

public class ModifiersUtils {

    private final int SYNTHETIC = 0x00001000;

    public String getModifiers(int modificatorIndex) {
        String modifiers = "";

        if (Modifier.isPublic(modificatorIndex)) modifiers += "public ";
        if (Modifier.isProtected(modificatorIndex)) modifiers += "protected ";
        if (Modifier.isPrivate(modificatorIndex)) modifiers += "private ";

        if (Modifier.isAbstract(modificatorIndex)) modifiers += "abstract ";

        if (Modifier.isSynchronized(modificatorIndex)) modifiers += "synchronized ";

        if (Modifier.isVolatile(modificatorIndex)) modifiers += "volatile ";
        if (Modifier.isTransient(modificatorIndex)) modifiers += "transient ";
        if (Modifier.isStrict(modificatorIndex)) modifiers += "strictfp ";

        if (Modifier.isStatic(modificatorIndex)) modifiers += "static ";
        if (Modifier.isNative(modificatorIndex)) modifiers += "native ";

        if (Modifier.isFinal(modificatorIndex)) modifiers += "final ";

        if (isSynthetic(modificatorIndex)) modifiers += "synthetic ";

        return modifiers;
    }

    private boolean isSynthetic(int modificatorIndex) {
        return (modificatorIndex & SYNTHETIC) != 0;
    }
}






















