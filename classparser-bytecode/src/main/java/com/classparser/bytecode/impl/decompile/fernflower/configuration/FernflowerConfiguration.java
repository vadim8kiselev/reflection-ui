package com.classparser.bytecode.impl.decompile.fernflower.configuration;

import com.classparser.bytecode.impl.decompile.fernflower.FernflowerDecompiler;
import com.classparser.configuration.Configuration;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer;
import org.jetbrains.java.decompiler.modules.renamer.ConverterHelper;

/**
 * Builder configuration for {@link FernflowerDecompiler}
 */
public interface FernflowerConfiguration extends Configuration {

    /**
     * Show bridge methods
     * <p>
     * Default value: true
     */
    FernflowerConfiguration showBridgeMethods(boolean flag);

    /**
     * Show synthetic class members
     * <p>
     * Default value: true
     */
    FernflowerConfiguration showMemberSyntheticClasses(boolean flag);

    /**
     * Decompile inner classes
     * <p>
     * Default value: true
     */
    FernflowerConfiguration decompileInnerClasses(boolean flag);

    /**
     * Collapse 1.4 class references
     * <p>
     * Default value: true
     */
    FernflowerConfiguration collapseClassReferences(boolean flag);

    /**
     * Decompile assertions
     * <p>
     * Default value: true
     */
    FernflowerConfiguration decompileAssertions(boolean flag);

    /**
     * Show empty super invocation
     * <p>
     * Default value: true
     */
    FernflowerConfiguration showEmptySuperInvocation(boolean flag);

    /**
     * Show empty default constructor
     * <p>
     * Default value: true
     */
    FernflowerConfiguration showEmptyDefaultConstructor(boolean flag);

    /**
     * Decompile generic signatures
     * <p>
     * Default value: true
     */
    FernflowerConfiguration decompileGenericSignatures(boolean flag);

    /**
     * Assume return not throwing exceptions
     * <p>
     * Default value: true
     */
    FernflowerConfiguration assumeReturnNotThrowingExceptions(boolean flag);

    /**
     * Decompile enumerations to class
     * <p>
     * Default value: true
     */
    FernflowerConfiguration decompileEnumerations(boolean flag);

    /**
     * Remove getClass() invocation, when it is part of a qualified new statement
     * <p>
     * Default value: true
     */
    FernflowerConfiguration removeGetClassInvocation(boolean flag);

    /**
     * Show output numeric literals "as-is"
     * <p>
     * Default value: false
     */
    FernflowerConfiguration showOutputNumericLiterals(boolean flag);

    /**
     * encode non-ASCII characters in string and character literals as Unicode escapes
     * <p>
     * Default value: false
     */
    FernflowerConfiguration encodeNonASCIICharacters(boolean flag);

    /**
     * Interpret int 1 as boolean true (workaround to a compiler bug)
     * <p>
     * Default value: true
     */
    FernflowerConfiguration interpretInt1AsBooleanTrue(boolean flag);

    /**
     * Allow for set synthetic attribute (workaround to a compiler bug)
     * <p>
     * Default value: false
     */
    FernflowerConfiguration allowForSetSyntheticAttribute(boolean flag);

    /**
     * Consider nameless types as java.lang.Object (workaround to a compiler architecture flaw)
     * <p>
     * Default value: false
     */
    FernflowerConfiguration considerNamelessTypes(boolean flag);

    /**
     * Reconstruct variable names from debug information, if present
     * <p>
     * Default value: true
     */
    FernflowerConfiguration reconstructVariableNamesFromDebugInformation(boolean flag);

    /**
     * Remove empty exception ranges
     * <p>
     * Default value: false
     */
    FernflowerConfiguration removeEmptyExceptionRanges(boolean flag);

    /**
     * De-inline finally structures
     * <p>
     * Default value: true
     */
    FernflowerConfiguration deInlineFinallyStructures(boolean flag);

    /**
     * Maximum allowed processing time per decompiled method, in seconds. 0 means no upper limit
     * <p>
     * Default value: non limited
     */
    FernflowerConfiguration setUpperLimitForDecompilation(int limit);

    /**
     * Rename ambiguous (resp. obfuscated) classes and class elements
     * <p>
     * Default value: false
     */
    FernflowerConfiguration renameAmbiguousClassesAndClassElements(boolean flag);

    /**
     * Full name of user-supplied class implementing IIdentifierRenamer.
     * It is used to determine which class identifiers should be renamed and provides
     * new identifier names (see "Renaming identifiers")
     * <p>
     * Default value:
     *
     * @see ConverterHelper
     */
    FernflowerConfiguration setNewIIdentifierRenamer(Class<? extends IIdentifierRenamer> renamer);

    /**
     * Check for IntelliJ IDEA-specific @NotNull annotation and remove inserted code if found
     * <p>
     * Default value: false
     */
    FernflowerConfiguration checkNonNullAnnotation(boolean flag);

    /**
     * Decompile lambda expressions to anonymous classes
     * <p>
     * Default value: false
     */
    FernflowerConfiguration decompileLambdaExpressionsToAnonymousClasses(boolean flag);

    /**
     * define new line character to be used for output.
     * '\r\n' (Windows),
     * '\n' (Unix)
     * <p>
     * Default value: selected by OS
     */
    FernflowerConfiguration defineLineSeparator(String character);

    /**
     * Indentation string
     * <p>
     * Default value: 4 spaces
     */
    FernflowerConfiguration setCountIndentSpaces(int indent);

    /**
     * Set logging level, possible values are TRACE, INFO, WARN, ERROR
     * <p>
     * Default value: ERROR
     */
    FernflowerConfiguration setLogLevel(IFernflowerLogger.Severity level);
}