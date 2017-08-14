package com.kiselev.classparser.impl.bytecode.agent;

import com.kiselev.classparser.impl.bytecode.utils.ClassNameUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/12/2017.
 */
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
