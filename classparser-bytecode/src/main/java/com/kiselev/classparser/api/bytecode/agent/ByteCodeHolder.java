package com.kiselev.classparser.api.bytecode.agent;

/**
 * Created by Aleksei Makarov on 08/04/2017.
 */
public interface ByteCodeHolder {

    /**
     * Getting byte code of class by class name
     *
     * @return Byte code of class
     */
    byte[] get(String className);
}
