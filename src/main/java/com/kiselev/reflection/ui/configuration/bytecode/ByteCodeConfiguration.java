package com.kiselev.reflection.ui.configuration.bytecode;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;

/**
 * Created by Алексей on 07/14/2017.
 *
 * Builder configuration for class
 * @see com.kiselev.reflection.ui.impl.bytecode.BytecodeParser
 */
public interface ByteCodeConfiguration extends Configuration {

    ByteCodeConfiguration decompileInnerClasses(boolean flag);

    ByteCodeConfiguration decompileInnerAndNestedClasses(boolean flag);

    ByteCodeConfiguration decompileAnonymousClasses(boolean flag);

    ByteCodeConfiguration decompileLocalClasses(boolean flag);

    ByteCodeConfiguration saveByteCodeToFile(boolean flag);

    ByteCodeConfiguration setDirectoryToSaveByteCode(String path);

    ByteCodeConfiguration addCustomByteCodeCollector(ByteCodeCollector collector);

    ByteCodeConfiguration addCustomDecompilerConfiguration(Configuration configuration);

    ByteCodeConfiguration addCustomDecompiler(Decompiler decompiler);

    ByteCodeConfiguration enableClassFileByteCodeCollector(boolean flag);

    ByteCodeConfiguration enableRetransformClassByteCodeCollector(boolean flag);

    ByteCodeConfiguration enableCustomByteCodeCollector(boolean flag);
}
