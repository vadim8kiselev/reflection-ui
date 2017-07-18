package com.kiselev.reflection.ui.impl.bytecode.collector;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public interface ByteCodeCollector {

    byte[] getByteCode(Class<?> clazz);
}
