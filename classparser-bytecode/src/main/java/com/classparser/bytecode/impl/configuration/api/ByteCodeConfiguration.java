package com.classparser.bytecode.impl.configuration.api;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.BytecodeParser;
import com.classparser.configuration.Configuration;

/**
 * Builder configuration for class: {@link BytecodeParser}
 */
public interface ByteCodeConfiguration extends Configuration {

    /**
     * Need decompile inner, nested, anonymous and local classes
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration decompileInnerClasses(boolean flag);

    /**
     * Need decompile inner and nested classes
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration decompileInnerAndNestedClasses(boolean flag);

    /**
     * Need decompile anonymous classes
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration decompileAnonymousClasses(boolean flag);

    /**
     * Need decompile local classes
     * WARNING: enable this option
     * can cause performance problem
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration decompileLocalClasses(boolean flag);

    /**
     * Saving collect bytecode to file
     * <p>
     * Default value: false
     */
    ByteCodeConfiguration saveByteCodeToFile(boolean flag);

    /**
     * Set directory to save bytecode of classes
     * <p>
     * Default value: "classes"
     */
    ByteCodeConfiguration setDirectoryToSaveByteCode(String path);

    /**
     * Append custom ByteCodeCollector
     * New collector starting first from all collectors
     * <p>
     * Default value: null
     */
    ByteCodeConfiguration addCustomByteCodeCollector(ByteCodeCollector collector);

    /**
     * Set custom decompile configuration
     * <p>
     * Default value: see choose decompiler
     */
    ByteCodeConfiguration setDecompilerConfiguration(Configuration configuration);

    /**
     * Set decompiler
     * <p>
     * Default value: FernflowerDecompiler
     */
    ByteCodeConfiguration setDecompiler(Decompiler decompiler);

    /**
     * Enable search bytecode from files
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration enableClassFileByteCodeCollector(boolean flag);

    /**
     * Enable getting bytecode from JVM
     * <p>
     * Default value: true
     */
    ByteCodeConfiguration enableFromJVMClassByteCodeCollector(boolean flag);

    /**
     * Enable custom collect of bytecode
     * <p>
     * Default value: false
     */
    ByteCodeConfiguration enableCustomByteCodeCollector(boolean flag);

    /**
     * Set custom agent class
     * <p>
     * Default value: Agent
     */
    ByteCodeConfiguration setAgentClass(JavaAgent agent);
}