package com.kiselev.reflection.ui.impl.modifier;

import java.lang.reflect.Modifier;

public class ModifiersUtils {

    private static final int BRIDGE = 0x00000040;

    private static final int MANDATED = 0x00008000;

    private final int SYNTHETIC = 0x00001000;

    private final int IMPLICIT = 0x8000;

    public String getModifiers(int modifierIndex) {
        String modifiers = "";

        if (Modifier.isPublic(modifierIndex)) modifiers += "public ";
        if (Modifier.isProtected(modifierIndex)) modifiers += "protected ";
        if (Modifier.isPrivate(modifierIndex)) modifiers += "private ";

        if (Modifier.isAbstract(modifierIndex)) modifiers += "abstract ";

        if (Modifier.isSynchronized(modifierIndex)) modifiers += "synchronized ";

        if (Modifier.isVolatile(modifierIndex)) modifiers += "volatile ";
        if (Modifier.isTransient(modifierIndex)) modifiers += "transient ";
        if (Modifier.isStrict(modifierIndex)) modifiers += "strictfp ";

        if (Modifier.isStatic(modifierIndex)) modifiers += "static ";
        if (Modifier.isNative(modifierIndex)) modifiers += "native ";

        if (isImplicit(modifierIndex)) modifiers += "implicit ";

        if (isSynthetic(modifierIndex)) modifiers += "synthetic ";

        if (isMandated(modifierIndex)) modifiers += "mandated ";

        if (isBridge(modifierIndex)) modifiers += "bridge ";

        if (Modifier.isFinal(modifierIndex)) modifiers += "final ";

        return modifiers;
    }

    private boolean isSynthetic(int modifierIndex) {
        return (modifierIndex & SYNTHETIC) != 0;
    }

    private boolean isImplicit(int modifierIndex) {
        return (modifierIndex & IMPLICIT) != 0;
    }

    private boolean isMandated(int mod) {
        return (mod & MANDATED) != 0;
    }

    private boolean isBridge(int mod) {
        return (mod & MANDATED) != 0;
    }
}






















