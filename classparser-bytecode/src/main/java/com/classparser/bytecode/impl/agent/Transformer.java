package com.classparser.bytecode.impl.agent;

import com.classparser.bytecode.api.agent.ByteCodeHolder;
import com.classparser.bytecode.impl.utils.ClassNameUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public final class Transformer implements ClassFileTransformer, ByteCodeHolder {

    private Map<String, byte[]> bytecodeMap = new HashMap<>();

    @Override
    public final byte[] transform(ClassLoader loader,
                                  String className,
                                  Class<?> classBeingRedefined,
                                  ProtectionDomain protectionDomain,
                                  byte[] byteCode) throws IllegalClassFormatException {
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
