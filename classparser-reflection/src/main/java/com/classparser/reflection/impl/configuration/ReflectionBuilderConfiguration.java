package com.classparser.reflection.impl.configuration;

import com.classparser.reflection.impl.configuration.api.ReflectionConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ReflectionBuilderConfiguration {

    public static ReflectionConfiguration configure() {
        return new Builder();
    }

    private static class Builder implements ReflectionConfiguration {

        private final HashMap<String, Object> configuration = new HashMap<>();

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public ReflectionConfiguration showAnnotationTypes(boolean flag) {
            configuration.put("sat", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration showInnerClasses(boolean flag) {
            configuration.put("sic", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration showNonJavaModifiers(boolean flag) {
            configuration.put("njm", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration showDefaultValueInAnnotation(boolean flag) {
            configuration.put("dva", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration showGenericSignatures(boolean flag) {
            configuration.put("sgs", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration showVarArgs(boolean flag) {
            configuration.put("sva", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration displayValueForFields(boolean flag) {
            configuration.put("dvf", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration enableImports(boolean flag) {
            configuration.put("dim", flag);
            return this;
        }

        @Override
        public ReflectionConfiguration setCountIndentSpaces(int indent) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < indent; i++) {
                builder.append(" ");
            }

            configuration.put("cis", builder.toString());
            return this;
        }

        @Override
        public ReflectionConfiguration defineLineSeparator(String character) {
            if (character.equals("\n")) {
                configuration.put("nlc", character);
            } else if (character.equals("\n\r")) {
                configuration.put("nlc", character);
            }

            return this;
        }
    }
}
