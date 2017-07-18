package com.kiselev.reflection.ui.impl.bytecode.collector;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public interface ByteCodeCollector {

    /**
     * Get bytecode of class
     *
     * @param clazz - class for which getting bytecode
     * @return bytecode of class
     */
    byte[] getByteCode(Class<?> clazz);
}
