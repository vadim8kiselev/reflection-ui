package com.kiselev.reflection.ui.impl.bytecode.decompile;

import com.kiselev.reflection.ui.configuration.Configuration;

import java.util.Collection;

/**
 * Created by Aleksei Makarov on 06/24/2017.
 */
public interface Decompiler {

    /**
     * Decompicating bytecode
     * <p>
     * @param byteCode - bytecode of class
     * @return pseudo source code
     * */
    String decompile(byte[] byteCode);

    /**
     * Set decompiler configuration
     * <p>
     * @param configuration - decompiler configuration
     * */
    void setConfiguration(Configuration configuration);

    /**
     * Add inner classes for current decompiling class
     * <p>
     * @param classes - bytecode of inner classes
     * */
    void appendAdditionalClasses(Collection<byte[]> classes);
}
