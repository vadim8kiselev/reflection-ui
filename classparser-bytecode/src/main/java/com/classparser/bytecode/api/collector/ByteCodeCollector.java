package com.classparser.bytecode.api.collector;

public interface ByteCodeCollector {

    /**
     * Trying find bytecode of class
     *
     * @param clazz - class for which is getting bytecode
     * @return bytecode of class or null if bytecode is not found
     */
    byte[] getByteCode(Class<?> clazz);
}