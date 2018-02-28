package com.kiselev.classparser.impl.bytecode.decompile.procyon.configuration;

import com.kiselev.classparser.configuration.Configuration;
import com.kiselev.classparser.impl.bytecode.decompile.procyon.ProcyonDecompiler;
import com.strobel.decompiler.languages.Language;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;
import com.strobel.decompiler.languages.java.JavaLanguage;

/**
 * Builder configuration for {@link ProcyonDecompiler}
 */
public interface ProcyonConfiguration extends Configuration {

    /**
     * Upload bytecode of references classes
     * for create annotation @Override
     * <p>
     * Default value: true
     */
    ProcyonConfiguration uploadClassReference(boolean flag);

    /**
     * Exclude nested types
     * <p>
     * Default value: false
     */
    ProcyonConfiguration excludeNestedTypes(boolean flag);

    /**
     * Decompile switch blocks
     * <p>
     * Default value: true
     */
    ProcyonConfiguration flattenSwitchBlocks(boolean flag);

    /**
     * Force explicit imports
     * <p>
     * Default value: true
     */
    ProcyonConfiguration forceExplicitImports(boolean flag);

    /**
     * Force explicit type arguments
     * <p>
     * Default value: true
     */
    ProcyonConfiguration forceExplicitTypeArguments(boolean flag);

    /**
     * Set language to decompile
     * <p>
     * Default value:
     *
     * @see JavaLanguage
     */
    ProcyonConfiguration setLanguage(Language language);

    /**
     * Set java formatter options
     * <p>
     * Default value:
     * Default by procyon decompiler plus
     * options.ClassBraceStyle default BraceStyle.EndOfLine;
     * options.InterfaceBraceStyle default BraceStyle.EndOfLine;
     * options.EnumBraceStyle default BraceStyle.EndOfLine;
     */
    ProcyonConfiguration setJavaFormatterOptions(JavaFormattingOptions language);

    /**
     * Display synthetic members
     * <p>
     * Default value: true
     */
    ProcyonConfiguration showSyntheticMembers(boolean flag);

    /**
     * Always generate exception variable for catch blocks
     * <p>
     * Default value: true
     */
    ProcyonConfiguration alwaysGenerateExceptionVariableForCatchBlocks(boolean flag);

    /**
     * Include error diagnostics
     * <p>
     * Default value: true
     */
    ProcyonConfiguration includeErrorDiagnostics(boolean flag);

    /**
     * <p>
     * Default value: false
     */
    ProcyonConfiguration includeLineNumbersInBytecode(boolean flag);

    /**
     * Retain redundant casts
     * <p>
     * Default value: true
     */
    ProcyonConfiguration retainRedundantCasts(boolean flag);

    /**
     * Retain pointless switches
     * <p>
     * Default value: true
     */
    ProcyonConfiguration retainPointlessSwitches(boolean flag);

    /**
     * Unicode output enabled
     * <p>
     * Default value: true
     */
    ProcyonConfiguration unicodeOutputEnabled(boolean flag);

    /**
     * Display debug line numbers
     * <p>
     * Default value: false
     */
    ProcyonConfiguration showDebugLineNumbers(boolean flag);

    /**
     * Merge variables
     * <p>
     * Default value: false
     */
    ProcyonConfiguration mergeVariables(boolean flag);

    /**
     * Simplify member references
     * <p>
     * Default value: true
     */
    ProcyonConfiguration simplifyMemberReferences(boolean flag);

    /**
     * Disable foreach transforms
     * <p>
     * Default value: false
     */
    ProcyonConfiguration disableForEachTransforms(boolean flag);
}
