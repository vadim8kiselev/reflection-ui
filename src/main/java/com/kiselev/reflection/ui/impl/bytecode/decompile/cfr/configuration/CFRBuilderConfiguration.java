package com.kiselev.reflection.ui.impl.bytecode.decompile.cfr.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/26/2017.
 */
public class CFRBuilderConfiguration {

    public static CFRConfiguration getBuilderConfiguration() {
        return new CFRBuilderConfiguration.Builder();
    }

    private static class Builder implements CFRConfiguration {

        private Map<String, Object> configuration = new HashMap<>();

        private Builder() {
        }

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public CFRConfiguration replaceStringConcatToStringBuilder(boolean flag) {
            configuration.put("stringbuilder", !flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarEnumInSwitch(boolean flag) {
            configuration.put("decodeenumswitch", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarInEnums(boolean flag) {
            configuration.put("sugarenums", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarStringInEnums(boolean flag) {
            configuration.put("decodestringswitch", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarInArrayIteration(boolean flag) {
            configuration.put("arrayiter", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarInCollectionIteration(boolean flag) {
            configuration.put("collectioniter", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileLambdaFunctions(boolean flag) {
            configuration.put("decodelambdas", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileInnerClasses(boolean flag) {
            configuration.put("innerclasses", flag);
            return this;
        }

        @Override
        public CFRConfiguration hideUTF8Characters(boolean flag) {
            configuration.put("hideutf", flag);
            return this;
        }

        @Override
        public CFRConfiguration hideVeryLongStrings(boolean flag) {
            configuration.put("hidelongstrings", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeBoilerplateFunctions(boolean flag) {
            configuration.put("removeboilerplate", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeInnerClassesSynthetics(boolean flag) {
            configuration.put("removeinnerclasssynthetics", flag);
            return this;
        }

        @Override
        public CFRConfiguration hideBridgeMethods(boolean flag) {
            configuration.put("hidebridgemethods", flag);
            return this;
        }

        @Override
        public CFRConfiguration liftInitialisationToAllConstructors(boolean flag) {
            configuration.put("liftconstructorinit", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeDeadMethods(boolean flag) {
            configuration.put("removedeadmethods", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeBadGenerics(boolean flag) {
            configuration.put("removebadgenerics", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarInAsserts(boolean flag) {
            configuration.put("sugarasserts", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileBoxing(boolean flag) {
            configuration.put("sugarboxing", flag);
            return this;
        }

        @Override
        public CFRConfiguration showCFRVersion(boolean flag) {
            configuration.put("showversion", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSugarInFinally(boolean flag) {
            configuration.put("decodefinally", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeSupportCodeForMonitors(boolean flag) {
            configuration.put("tidymonitors", flag);
            return this;
        }

        @Override
        public CFRConfiguration replaceMonitorWithComments(boolean flag) {
            configuration.put("commentmonitors", flag);
            return this;
        }

        @Override
        public CFRConfiguration lenientSituationsWhereThrowException(boolean flag) {
            configuration.put("lenient", flag);
            return this;
        }

        @Override
        public CFRConfiguration dumpClassPathForDebuggingPurposes(boolean flag) {
            configuration.put("dumpclasspath", flag);
            return this;
        }

        @Override
        public CFRConfiguration showDecompilerMessages(boolean flag) {
            configuration.put("comments", flag);
            return this;
        }

        @Override
        public CFRConfiguration forceBasicBlockSorting(boolean flag) {
            configuration.put("forcetopsort", flag);
            return this;
        }

        @Override
        public CFRConfiguration allowForLoopsToAggressivelyRollMutations(boolean flag) {
            configuration.put("forloopaggcapture", flag);
            return this;
        }

        @Override
        public CFRConfiguration forceTopSortAggress(boolean flag) {
            configuration.put("forcetopsortaggress", flag);
            return this;
        }

        @Override
        public CFRConfiguration forceCodePropagate(boolean flag) {
            configuration.put("forcecondpropagate", flag);
            return this;
        }

        @Override
        public CFRConfiguration forceReturnIngifs(boolean flag) {
            configuration.put("forcereturningifs", flag);
            return this;
        }

        @Override
        public CFRConfiguration forceExceptionPrune(boolean flag) {
            configuration.put("forceexceptionprune", flag);
            return this;
        }

        @Override
        public CFRConfiguration removeNestedExceptionsHandlers(boolean flag) {
            configuration.put("aexagg", flag);
            return this;
        }

        @Override
        public CFRConfiguration splitLifetimesAnalysisCausedType(boolean flag) {
            configuration.put("recovertypeclash", flag);
            return this;
        }

        @Override
        public CFRConfiguration recoverTypeHintsForIterators(boolean flag) {
            configuration.put("recovertypehints", flag);
            return this;
        }

        @Override
        public CFRConfiguration showDebugInfo(int debug) {
            configuration.put("showops", debug);
            return this;
        }

        @Override
        public CFRConfiguration doNotDisplayStateWhile(boolean flag) {
            configuration.put("silent", flag);
            return this;
        }

        @Override
        public CFRConfiguration allowMoreAggressiveOptions(boolean flag) {
            configuration.put("recover", flag);
            return this;
        }

        @Override
        public CFRConfiguration enableEclipseTransformations(boolean flag) {
            configuration.put("eclipse", flag);
            return this;
        }

        @Override
        public CFRConfiguration generateOverrideAnnotations(boolean flag) {
            configuration.put("override", flag);
            return this;
        }

        @Override
        public CFRConfiguration decorateMethodsWithExplicitTypes(boolean flag) {
            configuration.put("showinferrable", flag);
            return this;
        }

        @Override
        public CFRConfiguration allowTransformationsWhichCorrectErrors(boolean flag) {
            configuration.put("allowcorrecting", flag);
            return this;
        }

        @Override
        public CFRConfiguration allowCodeUsesLabelledBlocks(boolean flag) {
            configuration.put("labelledblocks", flag);
            return this;
        }

        @Override
        public CFRConfiguration reverseOldJavaClassObjectConstruction(boolean flag) {
            configuration.put("j14classobj", flag);
            return this;
        }

        @Override
        public CFRConfiguration hideDefaultImports(boolean flag) {
            configuration.put("hidelangimports", flag);
            return this;
        }

        @Override
        public CFRConfiguration decompileSpecificallyWithRecoveryOptions(int debug) {
            configuration.put("recpass", debug);
            return this;
        }

        @Override
        public CFRConfiguration renameAll(boolean flag) {
            configuration.put("rename", flag);
            return this;
        }

        @Override
        public CFRConfiguration renameDuplicateFields(boolean flag) {
            configuration.put("renamedupmembers", flag);
            return this;
        }

        @Override
        public CFRConfiguration renameSmallMembers(int rename) {
            configuration.put("renamesmallmembers", rename);
            return this;
        }

        @Override
        public CFRConfiguration renameInvalidIdentifiers(boolean flag) {
            configuration.put("renameillegalidents", flag);
            return this;
        }

        @Override
        public CFRConfiguration renameEnumIdentifiers(boolean flag) {
            configuration.put("renameenumidents", flag);
            return this;
        }

        @Override
        public CFRConfiguration countAtWhichToTriggerAggressiveReductions(int opcode) {
            configuration.put("aggressivesizethreshold", opcode);
            return this;
        }

        @Override
        public CFRConfiguration removeReturnFromStaticInit(boolean flag) {
            configuration.put("staticinitreturn", flag);
            return this;
        }

        @Override
        public CFRConfiguration pullCodeIntoCaseStatements(boolean flag) {
            configuration.put("pullcodecase", flag);
            return this;
        }

        @Override
        public CFRConfiguration elideThingsInScalaOutput(boolean flag) {
            configuration.put("elidescala", flag);
            return this;
        }
    }
}
