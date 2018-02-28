package com.kiselev.classparser.api.bytecode.agent;

public interface ByteCodeHolder {

    /**
     * Getting byte code of class by class name
     *
     * @return Byte code of class
     */
    byte[] get(String className);
}
