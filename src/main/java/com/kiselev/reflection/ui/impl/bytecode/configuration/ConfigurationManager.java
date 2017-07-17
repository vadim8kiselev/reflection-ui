package com.kiselev.reflection.ui.impl.bytecode.configuration;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.configuration.bytecode.ByteCodeBuilderConfiguration;
import com.kiselev.reflection.ui.exception.ByteCodeParserException;
import com.kiselev.reflection.ui.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.fernflower.FernflowerDecompiler;

import java.io.File;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/17/2017.
 */
public class ConfigurationManager {

    private static ThreadLocal<Map<String, Object>> configurationLocalMap = new ThreadLocal<>();

    public static void setConfiguration(Map<String, Object> configuration) {
        Map<String, Object> stringObjectMap = configurationLocalMap.get();
        if (stringObjectMap == null) {
            configurationLocalMap.set(getDefaultConfiguration());
        }

        configurationLocalMap.get().putAll(configuration);
    }

    
    public static boolean isDecompileInnerClasses() {
        return (boolean)configurationLocalMap.get().get("dic");
    }

    public static boolean isDecompileInnerAndNestedClasses() {
        return (boolean)configurationLocalMap.get().get("din");
    }

    public static boolean isDecompileAnonymousClasses() {
        return (boolean)configurationLocalMap.get().get("dac");
    }

    public static boolean isDecompileLocalClasses() {
        return (boolean)configurationLocalMap.get().get("dlc");
    }

    public static boolean isSaveToFile() {
        return (boolean)configurationLocalMap.get().get("stf");
    }

    public static String getDirectoryForSaveBytecode() {
        return (String) configurationLocalMap.get().get("dts");
    }

    @SuppressWarnings("unchecked")
    public static ByteCodeCollector getCustomByteCodeCollector() {
        Map<String, Object> stringObjectMap = configurationLocalMap.get();
        Class<? extends ByteCodeCollector> byteCodeCollector = (Class<? extends ByteCodeCollector>) stringObjectMap.get("bcc");
        if (byteCodeCollector != null) {
            try {
                return byteCodeCollector.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new ByteCodeParserException("Can't create object by class: " + byteCodeCollector.getName(), exception);
            }
        } else {
            return null;
        }
    }
    
    public static Configuration getCustomDecompilerConfiguration() {
        return (Configuration) configurationLocalMap.get().get("cdc");
    }

    @SuppressWarnings("unchecked")
    public static Decompiler getDecompiler() {
        Map<String, Object> stringObjectMap = configurationLocalMap.get();
        Class<? extends Decompiler> decompilerClazz = (Class<? extends Decompiler>) stringObjectMap.get("acd");
        try {
            return decompilerClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new ByteCodeParserException("Can't create object by class: " + decompilerClazz.getName(), exception);
        }
    }

    public static boolean isEnableClassFileByteCodeCollector() {
        return (boolean)configurationLocalMap.get().get("cfc");
    }

    public static boolean isEnableRetransformClassByteCodeCollector() {
        return (boolean)configurationLocalMap.get().get("rcc");
    }

    public static boolean isEnableCustomByteCodeCollector() {
        return (boolean)configurationLocalMap.get().get("cbc");
    }

    private static Map<String, Object> getDefaultConfiguration() {
        return ByteCodeBuilderConfiguration
                .configure()
                .decompileInnerClasses(true)
                .decompileInnerAndNestedClasses(true)
                .decompileAnonymousClasses(true)
                .decompileLocalClasses(true)
                .addCustomDecompiler(FernflowerDecompiler.class)
                .enableClassFileByteCodeCollector(true)
                .enableRetransformClassByteCodeCollector(true)
                .saveByteCodeToFile(false)
                .enableCustomByteCodeCollector(false)
                .setDirectoryToSaveByteCode(System.getProperty(Constants.Properties.HOME_DIR) + File.separator + "classes")
                .getConfiguration();
    }
}
