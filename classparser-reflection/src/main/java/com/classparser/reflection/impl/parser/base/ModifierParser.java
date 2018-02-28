package com.classparser.reflection.impl.parser.base;

import com.classparser.reflection.impl.state.StateManager;

import java.lang.reflect.Modifier;

public class ModifierParser {

    private static final int SYNTHETIC = 0x00001000;

    private static final int IMPLICIT = 0x00008000;

    public static String getModifiers(int modifierIndex) {
        String modifiers = "";

        if (StateManager.getConfiguration().isShowNonJavaModifiers()) {
            if (isSynthetic(modifierIndex)) {
                modifiers += "synthetic ";
            }

            if (isImplicit(modifierIndex)) {
                modifiers += "implicit ";
            }
        }

        if (Modifier.isPublic(modifierIndex)) {
            modifiers += "public ";
        }
        if (Modifier.isProtected(modifierIndex)) {
            modifiers += "protected ";
        }
        if (Modifier.isPrivate(modifierIndex)) {
            modifiers += "private ";
        }

        if (Modifier.isAbstract(modifierIndex)) {
            modifiers += "abstract ";
        }

        if (Modifier.isSynchronized(modifierIndex)) {
            modifiers += "synchronized ";
        }

        if (Modifier.isVolatile(modifierIndex)) {
            modifiers += "volatile ";
        }
        if (Modifier.isTransient(modifierIndex)) {
            modifiers += "transient ";
        }
        if (Modifier.isStrict(modifierIndex)) {
            modifiers += "strictfp ";
        }

        if (Modifier.isStatic(modifierIndex)) {
            modifiers += "static ";
        }
        if (Modifier.isNative(modifierIndex)) {
            modifiers += "native ";
        }

        if (Modifier.isFinal(modifierIndex)) {
            modifiers += "final ";
        }

        return modifiers;
    }

    private static boolean isSynthetic(int modifierIndex) {
        return (modifierIndex & SYNTHETIC) != 0;
    }

    private static boolean isImplicit(int modifierIndex) {
        return (modifierIndex & IMPLICIT) != 0;
    }
}






















