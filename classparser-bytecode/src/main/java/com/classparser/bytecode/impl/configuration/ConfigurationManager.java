package com.classparser.bytecode.impl.configuration;

import com.classparser.bytecode.api.agent.JavaAgent;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.agent.Agent;
import com.classparser.bytecode.impl.assembly.build.constant.Constants;
import com.classparser.bytecode.impl.decompile.fernflower.FernflowerDecompiler;
import com.classparser.configuration.Configuration;
import com.classparser.configuration.util.ConfigurationUtils;

import java.io.File;
import java.util.Map;

public class ConfigurationManager {

    private final Map<String, Object> configuration;

    private ConfigurationUtils utils;

    ConfigurationManager() {
        this.configuration = getDefaultConfiguration();
        this.utils = new ConfigurationUtils(configuration, configuration);
    }

    ConfigurationManager(Map<String, Object> configuration) {
        this();
        this.configuration.putAll(configuration);
        this.utils.appendConfiguration(configuration);
    }

    private static Map<String, Object> getDefaultConfiguration() {
        String HOME_DIR = System.getProperty(Constants.Properties.HOME_DIR);
        return ByteCodeBuilderConfiguration
                .configure()
                .decompileInnerClasses(true)
                .decompileInnerAndNestedClasses(true)
                .decompileAnonymousClasses(true)
                .decompileLocalClasses(false)
                .setDecompiler(new FernflowerDecompiler())
                .enableClassFileByteCodeCollector(true)
                .enableFromJVMClassByteCodeCollector(true)
                .saveByteCodeToFile(false)
                .setDecompilerConfiguration(null)
                .addCustomByteCodeCollector(null)
                .enableCustomByteCodeCollector(false)
                .setDirectoryToSaveByteCode(HOME_DIR + File.separatorChar + "classes")
                .setAgentClass(new Agent())
                .getConfiguration();
    }

    void reloadConfiguration(Map<String, Object> configuration) {
        this.configuration.putAll(configuration);
        System.out.println(this.configuration);
        this.utils = new ConfigurationUtils(this.configuration, getDefaultConfiguration());
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
        return utils.getConfig("stf", Boolean.class);
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
