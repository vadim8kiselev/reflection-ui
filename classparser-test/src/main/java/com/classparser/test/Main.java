package com.classparser.test;

import com.classparser.api.ClassParser;
import com.classparser.bytecode.impl.BytecodeParser;
import com.classparser.bytecode.impl.configuration.ByteCodeBuilderConfiguration;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;
import com.classparser.bytecode.impl.decompile.procyon.ProcyonDecompiler;
import com.classparser.bytecode.impl.saver.ByteCodeSaver;
import com.classparser.reflection.impl.ReflectionParser;
import com.classparser.reflection.impl.configuration.ReflectionBuilderConfiguration;
import sun.security.action.GetPropertyAction;

import java.security.AccessController;
import java.util.PropertyPermission;

public class Main {

    static {
        System.setProperty("jdk.internal.lambda.dumpProxyClasses", "E:\\Users\\alma0317\\IdeaProjects\\classparser\\classes");
    }

    Runnable runnable = () -> run();

    public void run() {

    }

    public static void main(String[] args) throws Exception {
        Test t = new Test();
        t.method();
        t.method2();
        ClassParser parser = new BytecodeParser();
        parser.setConfiguration(ByteCodeBuilderConfiguration.configure().setDecompiler(new ProcyonDecompiler()).getConfiguration());
        parser.parseClass(t.getClass());


        new Main();
    }
}
