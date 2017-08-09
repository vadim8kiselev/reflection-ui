package com.kiselev.reflection.ui.impl.bytecode.decompile.procyon.configuration;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.strobel.decompiler.languages.Language;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;

/**
 * Created by Aleksei Makarov on 07/21/2017.
 * <p>
 * Builder configuration for
 *
 * @see com.kiselev.reflection.ui.impl.bytecode.decompile.procyon.ProcyonDecompiler
 */
public interface ProcyonConfiguration extends Configuration {

    ProcyonConfiguration uploadClassReference(boolean flag);

    ProcyonConfiguration excludeNestedTypes(boolean flag);

    ProcyonConfiguration flattenSwitchBlocks(boolean flag);

    ProcyonConfiguration forceExplicitImports(boolean flag);

    ProcyonConfiguration forceExplicitTypeArguments(boolean flag);

    ProcyonConfiguration setLanguage(Language language);

    ProcyonConfiguration setJavaFormatterOptions(JavaFormattingOptions language);

    ProcyonConfiguration showSyntheticMembers(boolean flag);

    ProcyonConfiguration alwaysGenerateExceptionVariableForCatchBlocks(boolean flag);

    ProcyonConfiguration includeErrorDiagnostics(boolean flag);

    ProcyonConfiguration includeLineNumbersInBytecode(boolean flag);

    ProcyonConfiguration retainRedundantCasts(boolean flag);

    ProcyonConfiguration retainPointlessSwitches(boolean flag);

    ProcyonConfiguration unicodeOutputEnabled(boolean flag);

    ProcyonConfiguration showDebugLineNumbers(boolean flag);

    ProcyonConfiguration mergeVariables(boolean flag);

    ProcyonConfiguration simplifyMemberReferences(boolean flag);

    ProcyonConfiguration disableForEachTransforms(boolean flag);
}
