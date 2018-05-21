package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.impl.utils.ClassNameUtils;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Transformer implements ClassFileTransformer, ByteCodeHolder {

    private final Map<String, byte[]> bytecodeMap;

    public Transformer() {
       this.bytecodeMap = new ConcurrentHashMap<>();
    }

    @Override
    public final byte[] transform(ClassLoader loader,
                                  String className,
                                  Class<?> classBeingRedefined,
                                  ProtectionDomain protectionDomain,
                                  byte[] byteCode) {
        uploadByteCodeOfClassToHolder(className, byteCode);
        return byteCode;
    }

    private void uploadByteCodeOfClassToHolder(String className, byte[] byteCode) {
        String javaBasedClassName = ClassNameUtils.normalizeFullName(className);
        bytecodeMap.put(javaBasedClassName, byteCode);
    }

    @Override
    public byte[] get(String className) {
        byte[] byteCode = bytecodeMap.get(className);
        bytecodeMap.remove(className);
        return byteCode;
    }
}