package com.classparser.bytecode.api.decompile;

import com.classparser.bytecode.impl.configuration.ConfigurationManager;
import com.classparser.bytecode.impl.decompile.fernflower.FernflowerDecompiler;
import com.classparser.configuration.Configuration;

import java.util.Collection;

/**
 * Interface provides methods for bytecode decompilation
 * Default Implementation is {@link FernflowerDecompiler}
 * Before decompilation will use method #setConfigurationManager
 * for setting parser configuration into decompiler
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

    /**
     * Method which called by ByteCodeParser for set configuration instance
     *
     * @param configurationManager current configuration manager
     */
    void setConfigurationManager(ConfigurationManager configurationManager);
}