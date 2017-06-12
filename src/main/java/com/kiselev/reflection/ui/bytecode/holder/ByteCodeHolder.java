package com.kiselev.reflection.ui.bytecode.holder;

import com.kiselev.reflection.ui.bytecode.assembly.AgentAssembler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/13/2017.
 */
public class ByteCodeHolder {

    private static Map<Class<?>, byte[]> byteCodeMap = new HashMap<>();

    public static void uploadByteCodeForClass(Class<?> clazz, byte[] byteCode) {
        byteCodeMap.put(clazz, byteCode);
    }

    public static String getDecompilledByteCode(Class<?> clazz) {
        loadByteCode(clazz);
        byte[] byteCode = byteCodeMap.get(clazz);
        return byteCode != null ? new String(byteCode) : "empty";
    }

    private static void loadByteCode(Class<?> clazz) {
        if (!AgentAssembler.isAssembled()) {
            AgentAssembler.assembly();
        }
    }
}
