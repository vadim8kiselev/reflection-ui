package com.classparser.bytecode.api.decompile;

import com.classparser.configuration.Configuration;

import java.util.Collection;

public interface Decompiler {

    /**
     * Process of decompiling bytecode
     * <p>
     *
     * @param byteCode - bytecode of class
     * @return decompiling bytecode
     */
    String decompile(byte[] byteCode);

    /**
     * Process of decompiling bytecode with inner classes
     * <p>
     *
     * @param byteCode - bytecode of class
     * @param classes  - bytecode of inner classes
     * @return decompiling bytecode
     */
    String decompile(byte[] byteCode, Collection<byte[]> classes);

    /**
     * Set decompiler configuration
     * <p>
     *
     * @param configuration - decompiler configuration
     */
    void setConfiguration(Configuration configuration);
}
