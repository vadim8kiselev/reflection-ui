package com.kiselev.reflection.ui.impl.bytecode.decompile;

import com.kiselev.reflection.ui.configuration.Configuration;

import java.util.Collection;

/**
 * Created by Aleksei Makarov on 06/24/2017.
 */
public interface Decompiler {

    String decompile(byte[] byteCode);

    void setConfiguration(Configuration configuration);

    void appendAdditionalClasses(Collection<byte[]> classes);
}
