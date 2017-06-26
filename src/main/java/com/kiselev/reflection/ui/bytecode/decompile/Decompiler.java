package com.kiselev.reflection.ui.bytecode.decompile;

import com.kiselev.reflection.ui.bytecode.decompile.configuration.Configuration;

import java.util.Collection;

/**
 * Created by Алексей on 24.06.2017.
 */
public interface Decompiler {

    String decompile(byte[] byteCode);

    void setConfiguration(Configuration configuration);

    void appendNestedClasses(Collection<byte[]> classes);

    void appendNestedClass(byte[] clazz);
}
