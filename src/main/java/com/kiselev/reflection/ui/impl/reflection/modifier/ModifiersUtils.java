package com.kiselev.reflection.ui.impl.reflection.modifier;

import java.lang.reflect.Modifier;

public class ModifiersUtils {

    private static final int BRIDGE = 0x00000040;

    private static final int MANDATED = 0x00008000;

    private static final int SYNTHETIC = 0x00001000;

    private static final int IMPLICIT = 0x8000;

    public String getModifiers(int modifierIndex) {
        String modifiers = "";

        if (isSynthetic(modifierIndex)) modifiers += "synthetic ";

        if (isBridge(modifierIndex)) modifiers += "bridge ";

        if (isMandated(modifierIndex)) modifiers += "mandated ";

        if (isImplicit(modifierIndex)) modifiers += "implicit ";

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

        if (Modifier.isFinal(modifierIndex)) modifiers += "final ";

        return modifiers;
    }

    private boolean isSynthetic(int modifierIndex) {
        return (modifierIndex & SYNTHETIC) != 0;
    }

    private boolean isImplicit(int modifierIndex) {
        return (modifierIndex & IMPLICIT) != 0;
    }

    private boolean isMandated(int modifierIndex) {
        return (modifierIndex & MANDATED) != 0;
    }

    private boolean isBridge(int modifierIndex) {
        return (modifierIndex & BRIDGE) != 0 && !Modifier.isVolatile(modifierIndex);
    }
}






















