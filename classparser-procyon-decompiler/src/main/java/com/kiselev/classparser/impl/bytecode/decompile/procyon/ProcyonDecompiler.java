package com.kiselev.classparser.impl.bytecode.decompile.procyon;

import com.kiselev.classparser.api.bytecode.collector.ByteCodeCollector;
import com.kiselev.classparser.api.bytecode.decompile.Decompiler;
import com.kiselev.classparser.configuration.Configuration;
import com.kiselev.classparser.configuration.util.ConfigurationUtils;
import com.kiselev.classparser.impl.bytecode.assembly.build.constant.Constants;
import com.kiselev.classparser.impl.bytecode.collector.ChainByteCodeCollector;
import com.kiselev.classparser.impl.bytecode.decompile.procyon.configuration.ProcyonBuilderConfiguration;
import com.kiselev.classparser.impl.bytecode.utils.ClassNameUtils;
import com.kiselev.classparser.impl.bytecode.utils.RuntimeLibraryUploader;
import com.strobel.assembler.metadata.Buffer;
import com.strobel.assembler.metadata.DeobfuscationUtilities;
import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.assembler.metadata.TypeReference;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.strobel.decompiler.languages.Language;
import com.strobel.decompiler.languages.java.BraceStyle;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;
import com.strobel.decompiler.languages.java.JavaLanguage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ProcyonDecompiler implements Decompiler {

    private static final String DECOMPILER_PATH = "/src/main/resources/procyon-decompiler-0.5.30.jar";

    private byte[] byteCode;

    private Map<String, Object> configuration = getDefaultConfiguration();

    private Map<String, byte[]> byteCodeMap = new HashMap<>();

    private ConfigurationUtils utils;

    @Override
    public String decompile(byte[] byteCode) {
        return decompile(byteCode, Collections.emptyList());
    }

    @Override
    public String decompile(byte[] byteCode, Collection<byte[]> classes) {
        RuntimeLibraryUploader.appendToClassPath(DECOMPILER_PATH);

        this.byteCode = byteCode;
        if (this.utils == null) {
            this.utils = new ConfigurationUtils(configuration, getDefaultConfiguration());
        }
        String className = ClassNameUtils.getClassName(byteCode);
        byteCodeMap.put(className, byteCode);
        appendAdditionalClasses(classes);

        PlainTextOutput output = new PlainTextOutput();
        DecompilerSettings settings = getDecompilerSettings();

        ITypeLoader typeLoader = new ProcyonTypeLoader();
        MetadataSystem metadataSystem = new MetadataSystem(typeLoader);
        TypeReference type = metadataSystem.lookupType(className);

        TypeDefinition resolvedType = type.resolve();
        DeobfuscationUtilities.processType(resolvedType);
        DecompilationOptions options = new DecompilationOptions();
        options.setSettings(settings);

        if (settings.getFormattingOptions() == null) {
            settings.setFormattingOptions(JavaFormattingOptions.createDefault());
        }

        settings.getLanguage().decompileType(resolvedType, output, options);

        return output.toString();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration.putAll(configuration.getConfiguration());
        this.utils = new ConfigurationUtils(this.configuration, getDefaultConfiguration());
    }

    private void appendAdditionalClasses(Collection<byte[]> classes) {
        for (byte[] byteCode : classes) {
            byteCodeMap.put(ClassNameUtils.getClassName(byteCode), byteCode);
        }
    }

    private DecompilerSettings getDecompilerSettings() {
        DecompilerSettings settings = new DecompilerSettings();

        settings.setExcludeNestedTypes(utils.getConfig("ent", Boolean.class));
        settings.setFlattenSwitchBlocks(utils.getConfig("fsb", Boolean.class));
        settings.setForceExplicitImports(utils.getConfig("fei", Boolean.class));
        settings.setForceExplicitTypeArguments(utils.getConfig("eta", Boolean.class));
        settings.setLanguage(utils.getConfig("lan", Language.class));
        settings.setFormattingOptions(utils.getConfig("jfo", JavaFormattingOptions.class));
        settings.setShowSyntheticMembers(utils.getConfig("ssm", Boolean.class));
        settings.setAlwaysGenerateExceptionVariableForCatchBlocks(utils.getConfig("gec", Boolean.class));
        settings.setIncludeErrorDiagnostics(utils.getConfig("ied", Boolean.class));
        settings.setIncludeLineNumbersInBytecode(utils.getConfig("iln", Boolean.class));
        settings.setRetainRedundantCasts(utils.getConfig("rrc", Boolean.class));
        settings.setRetainPointlessSwitches(utils.getConfig("rps", Boolean.class));
        settings.setUnicodeOutputEnabled(utils.getConfig("uoe", Boolean.class));
        settings.setShowDebugLineNumbers(utils.getConfig("sdl", Boolean.class));
        settings.setMergeVariables(utils.getConfig("mva", Boolean.class));
        settings.setSimplifyMemberReferences(utils.getConfig("smr", Boolean.class));
        settings.setDisableForEachTransforms(utils.getConfig("det", Boolean.class));

        return settings;
    }

    private Map<String, Object> getDefaultConfiguration() {
        JavaFormattingOptions options = JavaFormattingOptions.createDefault();
        options.ClassBraceStyle = BraceStyle.EndOfLine;
        options.InterfaceBraceStyle = BraceStyle.EndOfLine;
        options.EnumBraceStyle = BraceStyle.EndOfLine;

        return ProcyonBuilderConfiguration
                .getBuilderConfiguration()
                .uploadClassReference(true)
                .excludeNestedTypes(false)
                .flattenSwitchBlocks(true)
                .forceExplicitImports(true)
                .forceExplicitTypeArguments(true)
                .setLanguage(new JavaLanguage())
                .setJavaFormatterOptions(options)
                .showSyntheticMembers(true)
                .alwaysGenerateExceptionVariableForCatchBlocks(true)
                .includeErrorDiagnostics(false)
                .includeLineNumbersInBytecode(false)
                .retainRedundantCasts(true)
                .retainPointlessSwitches(true)
                .unicodeOutputEnabled(true)
                .showDebugLineNumbers(false)
                .mergeVariables(false)
                .simplifyMemberReferences(true)
                .disableForEachTransforms(false)
                .getConfiguration();
    }

    private class ProcyonTypeLoader implements ITypeLoader {

        private static final int START_POSITION = 0;

        private String outerClassName = ClassNameUtils.getClassName(byteCode);

        private boolean isLoadReferenceOnClass = utils.getConfig("ucr", Boolean.class);

        private ByteCodeCollector collector = new ChainByteCodeCollector();

        @Override
        public boolean tryLoadType(String baseClassName, Buffer buffer) {
            byte[] byteCode = byteCodeMap.get(baseClassName);

            if (byteCode == null) {
                if (isLoadReferenceOnClass) {
                    if (baseClassName.contains(outerClassName + Constants.Symbols.DOLLAR)) {
                        return false;
                    }

                    byteCode = loadByteCode(ClassNameUtils.normalizeFullName(baseClassName));
                    if (byteCode == null) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            buffer.putByteArray(byteCode, START_POSITION, byteCode.length);
            buffer.position(START_POSITION);

            return true;
        }

        private Class<?> loadClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException exception) {
                return null;
            }
        }

        private byte[] loadByteCode(String className) {
            Class<?> clazz = loadClass(className);

            if (clazz != null) {
                byte[] byteCode = collector.getByteCode(clazz);

                if (byteCode != null) {
                    return byteCode;
                }
            }

            return null;
        }
    }
}
