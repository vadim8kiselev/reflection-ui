package com.classparser.bytecode.impl.configuration;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.configuration.api.ByteCodeConfiguration;
import com.classparser.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ByteCodeBuilderConfiguration {

    public static ByteCodeConfiguration configure() {
        return new Builder();
    }

    private static class Builder implements ByteCodeConfiguration {

        private final HashMap<String, Object> configuration = new HashMap<>();

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public ByteCodeConfiguration decompileInnerClasses(boolean flag) {
            configuration.put("dic", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration decompileInnerAndNestedClasses(boolean flag) {
            configuration.put("din", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration decompileAnonymousClasses(boolean flag) {
            configuration.put("dac", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration decompileLocalClasses(boolean flag) {
            configuration.put("dlc", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration saveByteCodeToFile(boolean flag) {
            configuration.put("stf", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration setDirectoryToSaveByteCode(String path) {
            configuration.put("dts", path);
            return this;
        }

        @Override
        public ByteCodeConfiguration addCustomByteCodeCollector(ByteCodeCollector collector) {
            configuration.put("bcc", collector);
            return this;
        }

        @Override
        public ByteCodeConfiguration setDecompilerConfiguration(Configuration configuration) {
            this.configuration.put("cdc", configuration);
            return this;
        }

        @Override
        public ByteCodeConfiguration setDecompiler(Decompiler decompiler) {
            configuration.put("acd", decompiler);
            return this;
        }

        @Override
        public ByteCodeConfiguration enableClassFileByteCodeCollector(boolean flag) {
            configuration.put("cfc", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration enableFromJVMClassByteCodeCollector(boolean flag) {
            configuration.put("rcc", flag);
            return this;
        }

        @Override
        public ByteCodeConfiguration enableCustomByteCodeCollector(boolean flag) {
            configuration.put("cbc", flag);
            return this;
        }

        public ByteCodeConfiguration setAgentClass(JavaAgent agent) {
            configuration.put("jaa", agent);
            return this;
        }

    }
}