package com.kiselev.classparser.impl.bytecode;

import com.kiselev.classparser.api.ClassParser;
import com.kiselev.classparser.api.bytecode.collector.ByteCodeCollector;
import com.kiselev.classparser.api.bytecode.decompile.Decompiler;
import com.kiselev.classparser.configuration.Configuration;
import com.kiselev.classparser.exception.agent.InvalidRetransformClass;
import com.kiselev.classparser.impl.bytecode.collector.ChainByteCodeCollector;
import com.kiselev.classparser.impl.bytecode.configuration.StateManager;
import com.kiselev.classparser.impl.bytecode.saver.ByteCodeSaver;
import com.kiselev.classparser.impl.bytecode.utils.InnerClassesCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/26/2017.
 */
public class BytecodeParser implements ClassParser {

    private ByteCodeCollector byteCodeCollector;

    @Override
    public String parseClass(Class<?> clazz) {
        checkToCorrectClass(clazz);
        if (byteCodeCollector == null) {
            byteCodeCollector = new ChainByteCodeCollector();
        }

        byte[] byteCode = getByteCodeOfClass(clazz);
        List<byte[]> bytecodeOfInnerClasses = getByteCodeOfInnerClasses(clazz);

        saveByteCodeToFile(byteCode, bytecodeOfInnerClasses);

        Decompiler decompiler = StateManager.getConfiguration().getDecompiler();
        Configuration configuration = StateManager.getConfiguration().getCustomDecompilerConfiguration();
        if (configuration != null) {
            decompiler.setConfiguration(configuration);
        }


        return decompiler.decompile(byteCode, bytecodeOfInnerClasses);
    }

    private void saveByteCodeToFile(byte[] byteCode, List<byte[]> bytecodeOfInnerClasses) {
        if (StateManager.getConfiguration().isSaveToFile()) {
            Thread thread = new Thread(() -> {
                ByteCodeSaver saver = new ByteCodeSaver();

                saver.saveToFile(byteCode);
                for (byte[] bytecodeOfInnerClass : bytecodeOfInnerClasses) {
                    saver.saveToFile(bytecodeOfInnerClass);
                }
            });
            thread.start();
        }
    }

    private List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz) {
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
            return new ArrayList<>();
        }
    }

    private byte[] getByteCodeOfClass(Class<?> clazz) {
        byte[] byteCode = byteCodeCollector.getByteCode(clazz);

        if (byteCode == null) {
            throw new NullPointerException(String.format("Byte code of class: %s is not found!", clazz.getName()));
        }

        return byteCode;
    }

    private void checkToCorrectClass(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Class can't be a null!");
        }

        if (clazz.isPrimitive()) {
            throw new InvalidRetransformClass(String.format("Primitive type: %s can not be decompiled", clazz.getName()));
        }

        if (clazz.isArray()) {
            throw new InvalidRetransformClass(String.format("Array type: %s can not be decompiled", clazz.getSimpleName()));
        }
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        StateManager.registerConfiguration(configuration);
    }
}
