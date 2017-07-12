package com.kiselev.reflection.ui.impl.bytecode.collector;

import java.util.List;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public interface ByteCodeCollector {

    byte[] getByteCode(Class<?> clazz);

    List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz);
}
