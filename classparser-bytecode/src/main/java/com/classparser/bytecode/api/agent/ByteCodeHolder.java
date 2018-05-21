package com.classparser.bytecode.api.agent;

public interface ByteCodeHolder {

    /**
     * Getting bytecode of class by class full name
     *
     * @param className class full name
     * @return bytecode of class
     */
    byte[] get(String className);
}