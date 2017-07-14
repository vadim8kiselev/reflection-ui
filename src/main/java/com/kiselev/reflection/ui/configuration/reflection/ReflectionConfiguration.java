package com.kiselev.reflection.ui.configuration.reflection;

import com.kiselev.reflection.ui.configuration.Configuration;

/**
 * Created by Алексей on 07/14/2017.
 */
public interface ReflectionConfiguration extends Configuration {

    /**
     * Visible annotation types
     * example Target.TYPE_USE or Target.PARAMETER_TYPE
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showAnnotationTypes(boolean flag);

    /**
     * Visible inner classes
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showInnerClasses(boolean flag);

    /**
     * Visible non java modifiers
     * synthetic, implicit and bridge
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showNonJavaModifiers(boolean flag);

    /**
     * Show default value in annotations
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showDefaultValueInAnnotation(boolean flag);

    /**
     * Show generic signatures
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showGenericSignatures(boolean flag);

    /**
     * Indentation string
     * <p>
     * Default value: 4 spaces
     */
    ReflectionConfiguration setCountIndentSpaces(int count);

    /**
     * define new line character to be used for output.
     * '\r\n' (Windows),
     * '\n' (Unix), default is OS-dependent
     * <p>
     * Default value: selected by OS
     */
    ReflectionConfiguration defineNewLineCharacter(String character);
}
