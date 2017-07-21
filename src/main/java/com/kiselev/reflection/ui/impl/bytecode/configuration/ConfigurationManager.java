package com.kiselev.reflection.ui.impl.bytecode.configuration;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.configuration.bytecode.ByteCodeBuilderConfiguration;
import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.fernflower.FernflowerDecompiler;

import java.io.File;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/18/2017.
 */
public class ConfigurationManager {

    private Map<String, Object> configuration;

    public ConfigurationManager() {
        this.configuration = getDefaultConfiguration();
    }

    public ConfigurationManager(Map<String, Object> configuration) {
        this();
        this.configuration.putAll(configuration);
    }

    private boolean isInstance(Object object, Class<?> clazz) {
        return clazz.isInstance(object);
    }

    public boolean isDecompileInnerClasses() {
        Object config = configuration.get("dic");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("dic");
        } else {
            return (boolean)getDefaultConfiguration().get("dic");
        }
    }

    public boolean isDecompileInnerAndNestedClasses() {
        Object config = configuration.get("din");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("din");
        } else {
            return (boolean)getDefaultConfiguration().get("din");
        }
    }

    public boolean isDecompileAnonymousClasses() {
        Object config = configuration.get("dac");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("dac");
        } else {
            return (boolean)getDefaultConfiguration().get("dac");
        }
    }

    public boolean isDecompileLocalClasses() {
        Object config = configuration.get("dlc");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("dlc");
        } else {
            return (boolean)getDefaultConfiguration().get("dlc");
        }
    }

    public boolean isSaveToFile() {
        Object config = configuration.get("stf");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("stf");
        } else {
            return (boolean)getDefaultConfiguration().get("stf");
        }
    }

    public String getDirectoryForSaveBytecode() {
        Object config = configuration.get("dts");
        if (isInstance(config, String.class)) {
            return (String)configuration.get("dts");
        } else {
            return (String)getDefaultConfiguration().get("dts");
        }
    }

    public ByteCodeCollector getCustomByteCodeCollector() {
        Object config = configuration.get("bcc");
        if (isInstance(config, ByteCodeCollector.class)) {
            return (ByteCodeCollector)configuration.get("bcc");
        } else {
            return (ByteCodeCollector)getDefaultConfiguration().get("bcc");
        }
    }

    public Configuration getCustomDecompilerConfiguration() {
        Object config = configuration.get("cdc");
        if (isInstance(config, Configuration.class)) {
            return (Configuration)configuration.get("cdc");
        } else {
            return (Configuration)getDefaultConfiguration().get("cdc");
        }
    }

    public Decompiler getDecompiler() {
        Object config = configuration.get("acd");
        if (isInstance(config, Decompiler.class)) {
            return (Decompiler)configuration.get("acd");
        } else {
            return (Decompiler)getDefaultConfiguration().get("acd");
        }
    }

    public boolean isEnableClassFileByteCodeCollector() {
        Object config = configuration.get("cfc");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("cfc");
        } else {
            return (boolean)getDefaultConfiguration().get("cfc");
        }
    }

    public boolean isEnableRetransformClassByteCodeCollector() {
        Object config = configuration.get("rcc");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("rcc");
        } else {
            return (boolean)getDefaultConfiguration().get("rcc");
        }
    }

    public boolean isEnableCustomByteCodeCollector() {
        Object config = configuration.get("cbc");
        if (isInstance(config, Boolean.class)) {
            return (boolean)configuration.get("cbc");
        } else {
            return (boolean)getDefaultConfiguration().get("cbc");
        }
    }

    private static Map<String, Object> getDefaultConfiguration() {
        return ByteCodeBuilderConfiguration
                .configure()
                .decompileInnerClasses(true)
                .decompileInnerAndNestedClasses(true)
                .decompileAnonymousClasses(true)
                .decompileLocalClasses(true)
                .setDecompiler(new FernflowerDecompiler())
                .enableClassFileByteCodeCollector(true)
                .enableRetransformClassByteCodeCollector(true)
                .saveByteCodeToFile(false)
                .enableCustomByteCodeCollector(false)
                .setDirectoryToSaveByteCode(System.getProperty(Constants.Properties.HOME_DIR) + File.separator + "classes")
                .getConfiguration();
    }
}
