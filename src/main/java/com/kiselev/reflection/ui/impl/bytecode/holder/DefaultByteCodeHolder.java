package com.kiselev.reflection.ui.impl.bytecode.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 08/04/2017.
 */
public class DefaultByteCodeHolder implements ByteCodeHolder {

    private Map<String, byte[]> bytecodeMap = new HashMap<>();

    @Override
    public void put(String className, byte[] bytecode) {
        bytecodeMap.put(className, bytecode);
    }

    @Override
    public byte[] get(String className) {
        byte[] bytecode =  bytecodeMap.get(className);
        bytecodeMap.remove(className);

        return bytecode;
    }
}
