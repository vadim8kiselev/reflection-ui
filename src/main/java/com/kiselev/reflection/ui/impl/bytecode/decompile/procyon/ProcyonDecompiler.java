package com.kiselev.reflection.ui.impl.bytecode.decompile.procyon;

import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.DefaultByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.procyon.configuration.ProcyonBuilderConfiguration;
import com.kiselev.reflection.ui.impl.bytecode.utils.ClassNameUtils;

import com.strobel.assembler.metadata.ITypeLoader;
import com.strobel.assembler.metadata.MetadataSystem;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.assembler.metadata.TypeReference;
import com.strobel.assembler.metadata.DeobfuscationUtilities;
import com.strobel.assembler.metadata.Buffer;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.strobel.decompiler.languages.Language;
import com.strobel.decompiler.languages.java.BraceStyle;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;
import com.strobel.decompiler.languages.java.JavaLanguage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aleksei Makarov on 07/21/2017.
 */
public final class ProcyonDecompiler implements Decompiler {

    private byte[] byteCode;

    private Map<String, Object> configuration;

    private Map<String, byte[]> byteCodeMap;

    @Override
    public String decompile(byte[] byteCode) {
        this.byteCode = byteCode;
        String className = ClassNameUtils.getClassName(byteCode);
        byteCodeMap.put(className, byteCode);

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
        if (this.configuration == null) {
            this.configuration = getDefaultConfiguration();
        }

        this.configuration.putAll(configuration.getConfiguration());
    }

    @Override
    public void appendAdditionalClasses(Collection<byte[]> classes) {
        this.byteCodeMap = new HashMap<>();
        for (byte[] byteCode : classes) {
            byteCodeMap.put(ClassNameUtils.getClassName(byteCode), byteCode);
        }
    }

    private class ProcyonTypeLoader implements ITypeLoader {

        boolean isLoadReferenceOnClass = (boolean) configuration.get("ucr");

        @Override
        public boolean tryLoadType(String baseClassName, Buffer buffer) {
            byte[] byteCode = byteCodeMap.get(baseClassName);

            if (byteCode == null) {
                if (isLoadReferenceOnClass) {

                    if (baseClassName.contains(ClassNameUtils.getClassName(ProcyonDecompiler.this.byteCode))) {
                        return false;
                    }

                    String className = ClassNameUtils.normalizeFullName(baseClassName);
                    Class<?> clazz = loadClass(className);

                    if (clazz == null) {
                        return false;
                    } else {
                        ByteCodeCollector collector = new DefaultByteCodeCollector();
                        byteCode = collector.getByteCode(clazz);
                    }
                } else {
                    return false;
                }
            }

            buffer.putByteArray(byteCode, 0, byteCode.length);
            buffer.position(0);

            return true;
        }

        private Class<?> loadClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException exception) {
                return null;
            }
        }
    }

    private DecompilerSettings getDecompilerSettings() {
        if (this.configuration == null) {
            this.configuration = getDefaultConfiguration();
        }
        DecompilerSettings settings = new DecompilerSettings();
        settings.setExcludeNestedTypes((boolean) configuration.get("ent"));
        settings.setFlattenSwitchBlocks((boolean) configuration.get("fsb"));
        settings.setForceExplicitImports((boolean) configuration.get("fei"));
        settings.setForceExplicitTypeArguments((boolean) configuration.get("eta"));
        settings.setLanguage((Language)configuration.get("lan"));
        settings.setFormattingOptions((JavaFormattingOptions) configuration.get("jfo"));
        settings.setShowSyntheticMembers((boolean) configuration.get("ssm"));
        settings.setAlwaysGenerateExceptionVariableForCatchBlocks((boolean) configuration.get("gec"));
        settings.setIncludeErrorDiagnostics((boolean) configuration.get("ied"));
        settings.setIncludeLineNumbersInBytecode((boolean) configuration.get("iln"));
        settings.setRetainRedundantCasts((boolean) configuration.get("rrc"));
        settings.setRetainPointlessSwitches((boolean) configuration.get("rps"));
        settings.setUnicodeOutputEnabled((boolean) configuration.get("uoe"));
        settings.setShowDebugLineNumbers((boolean) configuration.get("sdl"));
        settings.setMergeVariables((boolean) configuration.get("mva"));
        settings.setSimplifyMemberReferences((boolean) configuration.get("smr"));
        settings.setDisableForEachTransforms((boolean) configuration.get("det"));

        return settings;
    }

    private Map<String, Object> getDefaultConfiguration() {
        JavaFormattingOptions options = JavaFormattingOptions.createDefault();
        options.ClassBraceStyle = BraceStyle.EndOfLine;
        options.InterfaceBraceStyle = BraceStyle.EndOfLine;

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
}
