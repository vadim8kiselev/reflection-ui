package com.kiselev.reflection.ui.bytecode.decompile.configuration;

import org.jetbrains.java.decompiler.main.extern.IIdentifierRenamer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 06/23/2017
 */
public class DecompilerConfiguration {

    public static FernflowerConfiguration getBuilderConfiguration() {
        return new Builder();
    }

    private static class Builder implements FernflowerConfiguration {

        private static final String ONE = "1";

        private static final String ZERO = "0";

        private Map<String, Object> configuration = new HashMap<>();

        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        public FernflowerConfiguration showBridgeMethods(boolean flag) {
            configuration.put("rbr", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration showMemberSyntheticClasses(boolean flag) {
            configuration.put("rsy", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration decompileInnerClasses(boolean flag) {
            configuration.put("din", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration collapseClassReferences(boolean flag) {
            configuration.put("dc4", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration decompileAssertions(boolean flag) {
            configuration.put("das", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration showEmptySuperInvocation(boolean flag) {
            configuration.put("hes", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration showEmptyDefaultConstructor(boolean flag) {
            configuration.put("hdc", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration decompileGenericSignatures(boolean flag) {
            configuration.put("dgs", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration assumeReturnNotThrowingExceptions(boolean flag) {
            configuration.put("ner", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration decompileEnumerations(boolean flag) {
            configuration.put("den", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration removeGetClassInvocation(boolean flag) {
            configuration.put("rgn", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration showOutputNumericLiterals(boolean flag) {
            configuration.put("lit", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration encodeNonASCIICharacters(boolean flag) {
            configuration.put("asc", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration interpretInt1AsBooleanTrue(boolean flag) {
            configuration.put("bto", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration allowForSetSyntheticAttribute(boolean flag) {
            configuration.put("nns", flag ? ZERO : ONE);
            return this;
        }

        public FernflowerConfiguration considerNamelessTypes(boolean flag) {
            configuration.put("uto", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration reconstructVariableNamesFromDebugInformation(boolean flag) {
            configuration.put("udv", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration removeEmptyExceptionRanges(boolean flag) {
            configuration.put("rer", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration deInlineFinallyStructures(boolean flag) {
            configuration.put("fdi", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration setUpperLimitForDecompilation(int limit) {
            configuration.put("mpm", String.valueOf(limit));
            return this;
        }

        public FernflowerConfiguration renameAmbiguousClassesAndClassElements(boolean flag) {
            configuration.put("ren", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration setNewIIdentifierRenamer(Class<? extends IIdentifierRenamer> renamer) {
            configuration.put("urc", renamer.getName());
            return this;
        }

        public FernflowerConfiguration checkNonNullAnnotation(boolean flag) {
            configuration.put("inn", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration decompileLambdaExpressionsToAnonymousClasses(boolean flag) {
            configuration.put("lac", flag ? ONE : ZERO);
            return this;
        }

        public FernflowerConfiguration defineNewLineCharacter(String character) {
            if (character.equals("\n")) {
                configuration.put("nls", ONE);
            } else if (character.contains("\r\n")) {
                configuration.put("nls", ZERO);
            }

            return this;
        }

        public FernflowerConfiguration setCountIndentSpaces(int indent) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < indent; i++) {
                builder.append(" ");
            }

            configuration.put("ind", builder.toString());
            return this;
        }

        public FernflowerConfiguration setLogLevel(LogLevel level) {
            configuration.put("log", level.name());
            return this;
        }
    }
}
