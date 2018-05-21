package com.classparser.bytecode.api.collector;

public interface ByteCodeCollector {

    /**
     * Get bytecode of class
     *
     * @param clazz - class for which getting bytecode
     * @return bytecode of class or null if bytecode is not found
     */
    byte[] getByteCode(Class<?> clazz);
}