package com.kiselev.reflection.ui.configuration.reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 07/14/2017.
 */
public class ReflectionParserConfiguration {

    public static ReflectionConfiguration configure() {
        return new Builder();
    }

    private static class Builder implements ReflectionConfiguration {

        private HashMap<String, Object> configuration = new HashMap<>();

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
        public ReflectionConfiguration setCountIndentSpaces(int indent) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < indent; i++) {
                builder.append(" ");
            }

            configuration.put("cis", builder.toString());
            return this;
        }

        @Override
        public ReflectionConfiguration defineNewLineCharacter(String character) {
            if (character.equals("\n")) {
                configuration.put("nlc", character);
            } else if (character.equals("\r\n")) {
                configuration.put("nlc", character);
            }

            return this;
        }
    }
}
