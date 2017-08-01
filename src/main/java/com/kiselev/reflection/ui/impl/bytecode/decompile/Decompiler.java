package com.kiselev.reflection.ui.impl.bytecode.decompile;

import com.kiselev.reflection.ui.configuration.Configuration;

import java.util.Collection;

/**
 * Created by Aleksei Makarov on 06/24/2017.
 */
public interface Decompiler {

    /**
     * Process of decompiling bytecode
     * <p>
     * @param byteCode - bytecode of class
     * @return decompiling bytecode
     * */
    String decompile(byte[] byteCode);

    /**
     * Process of decompiling bytecode with inner classes
     * <p>
     * @param byteCode - bytecode of class
     * @param classes - bytecode of inner classes
     * @return decompiling bytecode
     * */
    String decompile(byte[] byteCode, Collection<byte[]> classes);

    /**
     * Set decompiler configuration
     * <p>
     * @param configuration - decompiler configuration
     * */
    void setConfiguration(Configuration configuration);
}
