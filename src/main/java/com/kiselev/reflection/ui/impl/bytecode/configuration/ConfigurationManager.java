package com.kiselev.reflection.ui.impl.bytecode.configuration;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.configuration.bytecode.ByteCodeBuilderConfiguration;
import com.kiselev.reflection.ui.configuration.util.ConfigurationUtils;
import com.kiselev.reflection.ui.impl.bytecode.agent.Agent;
import com.kiselev.reflection.ui.impl.bytecode.agent.JavaAgent;
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

    private ConfigurationUtils utils;

    public ConfigurationManager() {
        this.configuration = getDefaultConfiguration();
        this.utils = new ConfigurationUtils(configuration, configuration);
    }

    public ConfigurationManager(Map<String, Object> configuration) {
        this();
        this.configuration.putAll(configuration);
        this.utils.appendConfiguration(configuration);
    }

    private static Map<String, Object> getDefaultConfiguration() {
        final String HOME_DIR = System.getProperty(Constants.Properties.HOME_DIR);
        return ByteCodeBuilderConfiguration
                .configure()
                .decompileInnerClasses(true)
                .decompileInnerAndNestedClasses(true)
                .decompileAnonymousClasses(true)
                .decompileLocalClasses(true)
                .setDecompiler(new FernflowerDecompiler())
                .enableClassFileByteCodeCollector(true)
                .enableFromJVMClassByteCodeCollector(true)
                .saveByteCodeToFile(false)
                .setDecompilerConfiguration(null)
                .addCustomByteCodeCollector(null)
                .enableCustomByteCodeCollector(false)
                .setDirectoryToSaveByteCode(HOME_DIR + File.separator + "classes")
                .setAgentClass(new Agent())
                .getConfiguration();
    }

    public boolean isDecompileInnerClasses() {
        return utils.getConfig("dic", Boolean.class);
    }

    public boolean isDecompileInnerAndNestedClasses() {
        return utils.getConfig("din", Boolean.class);
    }

    public boolean isDecompileAnonymousClasses() {
        return utils.getConfig("dac", Boolean.class);
    }

    public boolean isDecompileLocalClasses() {
        return utils.getConfig("dlc", Boolean.class);
    }

    public boolean isSaveToFile() {
        return utils.getConfig("cfc", Boolean.class);
    }

    public String getDirectoryForSaveBytecode() {
        return utils.getConfig("dts", String.class);
    }

    public ByteCodeCollector getCustomByteCodeCollector() {
        return utils.getConfig("bcc", ByteCodeCollector.class);
    }

    public Configuration getCustomDecompilerConfiguration() {
        return utils.getConfig("cdc", Configuration.class);
    }

    public Decompiler getDecompiler() {
        return utils.getConfig("acd", Decompiler.class);
    }

    public boolean isEnableClassFileByteCodeCollector() {
        return utils.getConfig("cfc", Boolean.class);
    }

    public boolean isEnableRetransformClassByteCodeCollector() {
        return utils.getConfig("rcc", Boolean.class);
    }

    public boolean isEnableCustomByteCodeCollector() {
        return utils.getConfig("cbc", Boolean.class);
    }

    public JavaAgent getAgent() {
        return utils.getConfig("jaa", JavaAgent.class);
    }
}
