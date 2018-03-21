package com.classparser.bytecode.impl;

import com.classparser.api.ClassParser;
import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.api.decompile.Decompiler;
import com.classparser.bytecode.impl.collector.ChainByteCodeCollector;
import com.classparser.bytecode.impl.configuration.StateManager;
import com.classparser.bytecode.impl.saver.ByteCodeSaver;
import com.classparser.bytecode.impl.utils.InnerClassesCollector;
import com.classparser.configuration.Configuration;
import com.classparser.exception.agent.InvalidRetransformClass;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BytecodeParser implements ClassParser {

    private final ByteCodeSaver saver = new ByteCodeSaver();

    @Override
    public String parseClass(Class<?> clazz) {
        checkToCorrectClass(clazz);

        ByteCodeCollector byteCodeCollector = new ChainByteCodeCollector();
        byte[] byteCode = getByteCodeOfClass(clazz, byteCodeCollector);
        List<byte[]> bytecodeOfInnerClasses = getByteCodeOfInnerClasses(clazz, byteCodeCollector);

        if (StateManager.getConfiguration().isSaveToFile()) {
            saveByteCodeToFile(byteCode, bytecodeOfInnerClasses);
        }

        Decompiler decompiler = StateManager.getConfiguration().getDecompiler();
        Configuration configuration = StateManager.getConfiguration().getCustomDecompilerConfiguration();
        if (configuration != null) {
            decompiler.setConfiguration(configuration);
        }

        return decompiler.decompile(byteCode, bytecodeOfInnerClasses);
    }

    private void saveByteCodeToFile(byte[] byteCode, List<byte[]> bytecodeOfInnerClasses) {
        this.saver.saveToFile(byteCode);
        for (byte[] bytecodeOfInnerClass : bytecodeOfInnerClasses) {
            this.saver.saveToFile(bytecodeOfInnerClass);
        }
    }

    private List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz, ByteCodeCollector byteCodeCollector) {
        if (StateManager.getConfiguration().isDecompileInnerClasses()) {
            List<byte[]> bytecodeOfInnerClasses = new ArrayList<>();

            for (Class<?> innerClass : InnerClassesCollector.getInnerClasses(clazz)) {
                byte[] byteCodeOfInnerClass = byteCodeCollector.getByteCode(innerClass);
                if (byteCodeOfInnerClass != null) {
                    bytecodeOfInnerClasses.add(byteCodeOfInnerClass);
                }
            }

            return bytecodeOfInnerClasses;
        } else {
            return Collections.emptyList();
        }
    }

    private byte[] getByteCodeOfClass(Class<?> clazz, ByteCodeCollector byteCodeCollector) {
        byte[] byteCode = byteCodeCollector.getByteCode(clazz);

        if (byteCode == null) {
            String message = MessageFormat.format("Byte code of class: \"{0}\" is not found!",
                    clazz.getName());
            throw new NullPointerException(message);
        }

        return byteCode;
    }

    private void checkToCorrectClass(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Class can't be a null!");
        }

        if (clazz.isPrimitive()) {
            String message = MessageFormat.format("Primitive type: \"{0}\" can not be decompiled",
                    clazz.getName());
            throw new InvalidRetransformClass(message);
        }

        if (clazz.isArray()) {
            String message = MessageFormat.format("Array type: \"{0}\" can not be decompiled",
                    clazz.getSimpleName());
            throw new InvalidRetransformClass(message);
        }
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        StateManager.registerConfiguration(configuration);
    }
}
