package com.kiselev.reflection.ui.impl.bytecode.decompile.cfr.configuration;

import com.kiselev.reflection.ui.configuration.Configuration;

/**
 * Created by Aleksei Makarov on 07/26/2017.
 *
 * Builder configuration for
 * @see com.kiselev.reflection.ui.impl.bytecode.decompile.cfr.CFRDecompiler
 */
public interface CFRConfiguration extends Configuration {

    /**
     * Convert new Stringbuilder().add.add.add to string + string + string
     * See http://www.benf.org/other/cfr/stringbuilder-vs-concatenation.html
     * <p>
     * Default value: false
     */
    CFRConfiguration replaceStringConcatToStringBuilder(boolean flag);

    /**
     * Re-sugar switch on enum
     * See http://www.benf.org/other/cfr/switch-on-enum.html
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarEnumInSwitch(boolean flag);

    /**
     * Re-sugar enums
     * See http://www.benf.org/other/cfr/how-are-enums-implemented.html
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarInEnums(boolean flag);

    /**
     * Re-sugar switch on String
     * See http://www.benf.org/other/cfr/java7switchonstring.html
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarStringInEnums(boolean flag);

    /**
     * Re-sugar array based iteration.
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarInArrayIteration(boolean flag);

    /**
     * Re-sugar collection based iteration
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarInCollectionIteration(boolean flag);

    /**
     * Re-build lambda functions
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileLambdaFunctions(boolean flag);

    /**
     * Decompile inner classes
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileInnerClasses(boolean flag);

    /**
     * Hide UTF8 characters - quote them instead of showing the raw characters
     * <p>
     * Default value: true
     */
    CFRConfiguration hideUTF8Characters(boolean flag);

    /**
     * Hide very long strings - useful if obfuscators have placed fake code in strings
     * <p>
     * Default value: false
     */
    CFRConfiguration hideVeryLongStrings(boolean flag);

    /**
     * Remove boilerplate functions - constructor boilerplate, lambda deserialisation etc
     * <p>
     * Default value: true
     */
    CFRConfiguration removeBoilerplateFunctions(boolean flag);

    /**
     * Remove (where possible) implicit outer class references in inner classes
     * <p>
     * Default value: true
     */
    CFRConfiguration removeInnerClassesSynthetics(boolean flag);

    /**
     * Hide bridge methods
     * <p>
     * Default value: true
     */
    CFRConfiguration hideBridgeMethods(boolean flag);

    /**
     * Lift initialisation code common to all constructors into member initialisation
     * <p>
     * Default value: true
     */
    CFRConfiguration liftInitialisationToAllConstructors(boolean flag);

    /**
     * Remove pointless methods - default constructor etc
     * <p>
     * Default value: false
     */
    CFRConfiguration removeDeadMethods(boolean flag);

    /**
     * Hide generics where we've obviously got it wrong, and fallback to non-generic
     * <p>
     * Default value: false
     */
    CFRConfiguration removeBadGenerics(boolean flag);

    /**
     * Re-sugar assert calls
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarInAsserts(boolean flag);

    /**
     * Where possible, remove pointless boxing wrappers
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileBoxing(boolean flag);

    /**
     * Show CFR version used in header (handy to turn off when regression testing)
     * <p>
     * Default value: false
     */
    CFRConfiguration showCFRVersion(boolean flag);

    /**
     * Re-sugar finally statements
     * <p>
     * Default value: true
     */
    CFRConfiguration decompileSugarInFinally(boolean flag);

    /**
     * Remove support code for monitors - eg catch blocks just to exit a monitor
     * <p>
     * Default value: false
     */
    CFRConfiguration removeSupportCodeForMonitors(boolean flag);

    /**
     * Replace monitors with comments - useful if we're completely confused
     * <p>
     * Default value: false
     */
    CFRConfiguration replaceMonitorWithComments(boolean flag);

    /**
     * Be a bit more lenient in situations where we'd normally throw an exception
     * <p>
     * Default value: true
     */
    CFRConfiguration lenientSituationsWhereThrowException(boolean flag);

    /**
     * Dump class path for debugging purposes
     * <p>
     * Default value: true
     */
    CFRConfiguration dumpClassPathForDebuggingPurposes(boolean flag);

    /**
     * Output comments describing decompiler status, fallback flags etc
     * <p>
     * Default value: true
     */
    CFRConfiguration showDecompilerMessages(boolean flag);

    /**
     * Force basic block sorting.  Usually not necessary for code emitted directly from javac,
     * but required in the case of obfuscation (or dex2jar!).  Will be enabled in recovery.
     * <p>
     * Default value: ?
     */
    CFRConfiguration forceBasicBlockSorting(boolean flag);

    /**
     * Allow for loops to aggressively roll mutations into update section,
     * even if they don't appear to be involved with the predicate
     * <p>
     * Default value: ?
     */
    CFRConfiguration allowForLoopsToAggressivelyRollMutations(boolean flag);

    /**
     * Force extra aggressive top sort options
     * <p>
     * Default value: ?
     */
    CFRConfiguration forceTopSortAggress(boolean flag);

    /**
     * Pull results of deterministic jumps back through some constant assignments
     * <p>
     * Default value: ?
     */
    CFRConfiguration forceCodePropagate(boolean flag);

    /**
     * Move return up to jump site
     * <p>
     * Default value: ?
     */
    CFRConfiguration forceReturnIngifs(boolean flag);

    /**
     * Try to extend and merge exceptions more aggressively
     * <p>
     * Default value: ?
     */
    CFRConfiguration forceExceptionPrune(boolean flag);

    /**
     * Remove nested exception handlers if they don't change semantics
     * <p>
     * Default value: ?
     */
    CFRConfiguration removeNestedExceptionsHandlers(boolean flag);

    /**
     * Split lifetimes where analysis caused type clash
     * <p>
     * Default value: ?
     */
    CFRConfiguration splitLifetimesAnalysisCausedType(boolean flag);

    /**
     * Recover type hints for iterators from first pass.
     * <p>
     * Default value: ?
     */
    CFRConfiguration recoverTypeHintsForIterators(boolean flag);

    /**
     * Show some (cryptic!) debug
     * <p>
     * Default value: ?
     */
    CFRConfiguration showDebugInfo(int debug);

    /**
     * Don't display state while decompiling
     * <p>
     * Default value: ?
     */
    CFRConfiguration doNotDisplayStateWhile(boolean flag);

    /**
     * Allow more and more aggressive options to be set if decompilation fails
     * <p>
     * Default value: ?
     */
    CFRConfiguration allowMoreAggressiveOptions(boolean flag);

    /**
     * Enable transformations to handle eclipse code better
     * <p>
     * Default value: ?
     */
    CFRConfiguration enableEclipseTransformations(boolean flag);

    /**
     * Generate @Override annotations
     * (if method is seen to implement interface method, or override a base class method)
     * <p>
     * Default value: ?
     */
    CFRConfiguration generateOverrideAnnotations(boolean flag);

    /**
     * Decorate methods with explicit types if not implied by arguments.
     * <p>
     * Default value: ?
     */
    CFRConfiguration decorateMethodsWithExplicitTypes(boolean flag);

    /**
     * Allow transformations which correct errors, potentially at the cost of altering emitted code behaviour.
     * An example would be removing impossible (in java!) exception handling - if this has any effect,
     * a warning will be emitted.
     * <p>
     * Default value: ?
     */
    CFRConfiguration allowTransformationsWhichCorrectErrors(boolean flag);

    /**
     * Allow code to be emitted which uses labelled blocks, (handling odd forward gotos)
     * <p>
     * Default value: ?
     */
    CFRConfiguration allowCodeUsesLabelledBlocks(boolean flag);

    /**
     * Reverse java 1.4 class object construction
     * <p>
     * Default value: ?
     */
    CFRConfiguration reverseOldJavaClassObjectConstruction(boolean flag);

    /**
     * Hide imports from java.lang.
     * <p>
     * Default value: ?
     */
    CFRConfiguration hideDefaultImports(boolean flag);

    /**
     * Decompile specifically with recovery options from pass #X.
     * (really only useful for debugging)
     * <p>
     * Default value: ?
     */
    CFRConfiguration decompileSpecificallyWithRecoveryOptions(int debug);

    /**
     * Synonym for 'renamedupmembers' + 'renameillegalidents' + 'renameenummembers'
     * <p>
     * Default value: ?
     */
    CFRConfiguration renameAll(boolean flag);

    /**
     * Rename ambiguous/duplicate fields.
     * Note - this WILL break reflection based access, so is not automatically enabled.
     * <p>
     * Default value: ?
     */
    CFRConfiguration renameDuplicateFields(boolean flag);

    /**
     * Rename small members.
     * Note - this WILL break reflection based access, so is not automatically enabled.
     * <p>
     * Default value: ?
     */
    CFRConfiguration renameSmallMembers(int rename);

    /**
     * Rename identifiers which are not valid java identifiers.
     * Note - this WILL break reflection based access, so is not automatically enabled.
     * <p>
     * Default value: ?
     */
    CFRConfiguration renameInvalidIdentifiers(boolean flag);

    /**
     * Rename ENUM identifiers which do not match their 'expected' string names.
     * Note - this WILL break reflection based access, so is not automatically enabled.
     * <p>
     * Default value: ?
     */
    CFRConfiguration renameEnumIdentifiers(boolean flag);

    /**
     * Opcode count at which to trigger aggressive reductions
     * <p>
     * Default value: ? //15000
     */
    CFRConfiguration countAtWhichToTriggerAggressiveReductions(int opcode);

    /**
     * Try to remove return from static init
     * <p>
     * Default value: ?
     */
    CFRConfiguration removeReturnFromStaticInit(boolean flag);

    /**
     * Pull code into case statements agressively
     * <p>
     * Default value: ?
     */
    CFRConfiguration pullCodeIntoCaseStatements(boolean flag);

    /**
     * Elide things which aren't helpful in scala output (serialVersionUID, @ScalaSignature)
     * <p>
     * Default value: ?
     */
    CFRConfiguration elideThingsInScalaOutput(boolean flag);
}
