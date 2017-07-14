package com.kiselev.reflection.ui.configuration.bytecode;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 07/14/2017.
 */
public class ByteCodeParserConfiguration {

    public static ByteCodeConfiguration configure() {
        return new Builder();
    }
    private static class Builder implements ByteCodeConfiguration {

        private HashMap<String, Object> configuration = new HashMap<>();

        @Override
        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        @Override
        public ByteCodeConfiguration decompileInnerClasses(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration decompileInnerAndNestedClasses(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration decompileAnonymousClasses(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration decompileLocalClasses(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration saveByteCodeToFile(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration setDirectoryToSaveByteCode(String path) {
            return null;
        }

        @Override
        public ByteCodeConfiguration addCustomByteCodeCollector(Class<? extends ByteCodeCollector> collector) {
            return null;
        }

        @Override
        public ByteCodeConfiguration addCustomDecompilerConfiguration(Class<? extends Configuration> configuration) {
            return null;
        }

        @Override
        public ByteCodeConfiguration addCustomDecompiler(Class<? extends Decompiler> decompiler) {
            return null;
        }

        @Override
        public ByteCodeConfiguration enableClassFileByteCodeCollector(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration enableRetransformClassByteCodeCollector(boolean flag) {
            return null;
        }

        @Override
        public ByteCodeConfiguration enableCustomByteCodeCollector(boolean flag) {
            return null;
        }
    }
}
