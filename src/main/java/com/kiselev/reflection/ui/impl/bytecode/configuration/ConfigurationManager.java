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
        this.configuration = getDefaultConfiguration();
        this.configuration.putAll(configuration);
    }

    public boolean isDecompileInnerClasses() {
        return (boolean)configuration.get("dic");
    }

    public boolean isDecompileInnerAndNestedClasses() {
        return (boolean)configuration.get("din");
    }

    public boolean isDecompileAnonymousClasses() {
        return (boolean)configuration.get("dac");
    }

    public boolean isDecompileLocalClasses() {
        return (boolean)configuration.get("dlc");
    }

    public boolean isSaveToFile() {
        return (boolean)configuration.get("stf");
    }

    public String getDirectoryForSaveBytecode() {
        return (String) configuration.get("dts");
    }

    public ByteCodeCollector getCustomByteCodeCollector() {
        return (ByteCodeCollector) configuration.get("bcc");
    }

    public Configuration getCustomDecompilerConfiguration() {
        return (Configuration) configuration.get("cdc");
    }

    public Decompiler getDecompiler() {
        return (Decompiler) configuration.get("acd");
    }

    public boolean isEnableClassFileByteCodeCollector() {
        return (boolean)configuration.get("cfc");
    }

    public boolean isEnableRetransformClassByteCodeCollector() {
        return (boolean)configuration.get("rcc");
    }

    public boolean isEnableCustomByteCodeCollector() {
        return (boolean)configuration.get("cbc");
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
