package com.kiselev.classparser.api.bytecode.collector;

public interface ByteCodeCollector {

    /**
     * Get bytecode of class
     *
     * @param clazz - class for which getting bytecode
     * @return bytecode of class or null of bytecode is not found
     */
    byte[] getByteCode(Class<?> clazz);
}
