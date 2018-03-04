package com.classparser.test;

import com.classparser.api.ClassParser;
import com.classparser.bytecode.impl.BytecodeParser;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {
        ClassParser parser = new BytecodeParser();
        System.out.println(parser.parseClass(Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), Runnable.class)));
    }
}
