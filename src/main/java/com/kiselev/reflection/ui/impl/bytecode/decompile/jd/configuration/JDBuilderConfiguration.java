package com.kiselev.reflection.ui.impl.bytecode.decompile.jd.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/19/2017.
 */
public class JDBuilderConfiguration {

    public static JDConfiguration getBuilderConfiguration() {
        return new JDBuilderConfiguration.Builder();
    }

    private static class Builder implements JDConfiguration {

        private Map<String, Object> configuration = new HashMap<>();

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public JDConfiguration showDefaultConstructor(boolean flag) {
            configuration.put("shc", flag);
            return this;
        }

        @Override
        public JDConfiguration realignmentLineNumber(boolean flag) {
            configuration.put("rln", flag);
            return this;
        }

        @Override
        public JDConfiguration showPrefixThis(boolean flag) {
            configuration.put("spt", flag);
            return this;
        }

        @Override
        public JDConfiguration mergeEmptyLines(boolean flag) {
            configuration.put("mel", flag);
            return this;
        }

        @Override
        public JDConfiguration unicodeEscape(boolean flag) {
            configuration.put("uce", flag);
            return this;
        }

        @Override
        public JDConfiguration showLineNumbers(boolean flag) {
            configuration.put("sln", flag);
            return this;
        }

        @Override
        public JDConfiguration setCountIndentSpaces(int indent) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < indent; i++) {
                builder.append(" ");
            }

            configuration.put("ind", builder.toString());
            return this;
        }
    }
}
