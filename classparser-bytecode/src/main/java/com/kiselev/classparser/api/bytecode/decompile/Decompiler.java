package com.kiselev.classparser.api.bytecode.decompile;

import com.kiselev.classparser.configuration.Configuration;

import java.util.Collection;

/**
 * Created by Aleksei Makarov on 06/24/2017.
 */
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
