package com.kiselev.reflection.ui.bytecode.decompile.configuration;

import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer;

import java.util.Map;

/**
 * Created by Алексей on 24.06.2017.
 */
public interface FernflowerConfiguration extends Configuration {

    /**
     * Get final configuration
     */
    Map<String, Object> getConfiguration();

    /**
     * Show bridge methods
     * <p>
     * Default value: false
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
     * Default value: false
     */
    FernflowerConfiguration showEmptySuperInvocation(boolean flag);

    /**
     * Show empty default constructor
     * <p>
     * Default value: false
     */
    FernflowerConfiguration showEmptyDefaultConstructor(boolean flag);

    /**
     * Decompile generic signatures
     * <p>
     * Default value: false
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
     * Default value: false
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
     * Default value: true
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
     * Default value: true
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
     * Default value: false
     */
    FernflowerConfiguration setNewIIdentifierRenamer(Class<? extends IIdentifierRenamer> renamer);

    /**
     * Check for IntelliJ IDEA-specific @NotNull annotation and remove inserted code if found
     * <p>
     * Default value: true
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
     * '\n' (Unix), default is OS-dependent
     * <p>
     * Default value: Windows
     */
    FernflowerConfiguration defineNewLineCharacter(String character);

    /**
     * Indentation string
     * <p>
     * Default value: 3 spaces
     */
    FernflowerConfiguration setCountIndentSpaces(int indent);

    /**
     * A logging level, possible values are TRACE, INFO, WARN, ERROR
     * <p>
     * Default value: INFO
     */
    FernflowerConfiguration setLogLevel(LogLevel level);
}
