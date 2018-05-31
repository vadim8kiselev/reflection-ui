package com.classparser.test;

import com.classparser.api.ClassParser;
import com.classparser.bytecode.impl.BytecodeParser;
import com.classparser.bytecode.impl.configuration.ByteCodeBuilderConfiguration;
import com.classparser.bytecode.impl.decompile.cfr.CFRDecompiler;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ClassParser parser = new BytecodeParser();
        Map<String, Object> configuration = ByteCodeBuilderConfiguration
                .configure()
                .enableClassFileByteCodeCollector(false)
                .setDecompiler(new CFRDecompiler())
                .getConfiguration();
        parser.setConfiguration(configuration);

        System.out.println(parser.parseClass(String.class));
    }
}
