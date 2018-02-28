package com.kiselev.classparser.impl.reflection.configuration.api;

import com.kiselev.classparser.configuration.Configuration;

/**
 * Created by Алексей on 07/14/2017.
 * <p>
 * Builder configuration for class: com.kiselev.classparser.impl.reflection.ReflectionParser
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
     * Default value: false
     */
    ReflectionConfiguration showNonJavaModifiers(boolean flag);

    /**
     * Show default value in annotations
     * <p>
     * Default value: false
     */
    ReflectionConfiguration showDefaultValueInAnnotation(boolean flag);

    /**
     * Show generic signatures
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showGenericSignatures(boolean flag);

    /**
     * Show variate arguments
     * <p>
     * Default value: true
     */
    ReflectionConfiguration showVarArgs(boolean flag);

    /**
     * Display value for fields
     * <p>
     * Default value: true
     */
    ReflectionConfiguration displayValueForFields(boolean flag);

    /**
     * Enable imports
     * <p>
     * Default value: true
     */
    ReflectionConfiguration enableImports(boolean flag);

    /**
     * Indentation string
     * <p>
     * Default value: 4 spaces
     */
    ReflectionConfiguration setCountIndentSpaces(int count);

    /**
     * define new line character to be used for output.
     * '\r\n' (Windows),
     * '\n' (Unix)
     * <p>
     * Default value: selected by OS
     */
    ReflectionConfiguration defineLineSeparator(String character);
}
