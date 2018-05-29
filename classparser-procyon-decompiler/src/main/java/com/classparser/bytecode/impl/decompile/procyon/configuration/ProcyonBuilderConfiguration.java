package com.classparser.bytecode.impl.decompile.procyon.configuration;

import com.strobel.decompiler.languages.Language;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;

import java.util.HashMap;
import java.util.Map;

public class ProcyonBuilderConfiguration {

    public static ProcyonConfiguration getBuilderConfiguration() {
        return new Builder();
    }

    private static class Builder implements ProcyonConfiguration {

        private final Map<String, Object> configuration = new HashMap<>();

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public ProcyonConfiguration uploadClassReference(boolean flag) {
            configuration.put("ucr", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration excludeNestedTypes(boolean flag) {
            configuration.put("ent", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration flattenSwitchBlocks(boolean flag) {
            configuration.put("fsb", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration forceExplicitImports(boolean flag) {
            configuration.put("fei", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration forceExplicitTypeArguments(boolean flag) {
            configuration.put("eta", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration setLanguage(Language language) {
            configuration.put("lan", language);
            return this;
        }

        @Override
        public ProcyonConfiguration setJavaFormatterOptions(JavaFormattingOptions language) {
            configuration.put("jfo", language);
            return this;
        }

        @Override
        public ProcyonConfiguration showSyntheticMembers(boolean flag) {
            configuration.put("ssm", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration alwaysGenerateExceptionVariableForCatchBlocks(boolean flag) {
            configuration.put("gec", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration includeErrorDiagnostics(boolean flag) {
            configuration.put("ied", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration includeLineNumbersInBytecode(boolean flag) {
            configuration.put("iln", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration retainRedundantCasts(boolean flag) {
            configuration.put("rrc", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration retainPointlessSwitches(boolean flag) {
            configuration.put("rps", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration unicodeOutputEnabled(boolean flag) {
            configuration.put("uoe", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration showDebugLineNumbers(boolean flag) {
            configuration.put("sdl", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration mergeVariables(boolean flag) {
            configuration.put("mva", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration simplifyMemberReferences(boolean flag) {
            configuration.put("smr", flag);
            return this;
        }

        @Override
        public ProcyonConfiguration disableForEachTransforms(boolean flag) {
            configuration.put("det", flag);
            return this;
        }
    }
}