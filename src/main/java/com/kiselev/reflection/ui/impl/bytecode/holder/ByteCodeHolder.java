package com.kiselev.reflection.ui.impl.bytecode.holder;

/**
 * Created by Aleksei Makarov on 08/04/2017.
 */
public interface ByteCodeHolder {

    void put(String name, byte[] bytecode);

    byte[] get(String className);
}
