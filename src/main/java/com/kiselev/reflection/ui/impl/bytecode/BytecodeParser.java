package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.ChainByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.saver.ByteCodeSaver;
import com.kiselev.reflection.ui.impl.bytecode.utils.InnerClassesCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Vadim Kiselev on 6/26/2017.
 */
public class BytecodeParser implements ReflectionUI {

    private ByteCodeCollector byteCodeCollector;

    @Override
    public String parseClass(Class<?> clazz) {
        byteCodeCollector = new ChainByteCodeCollector();
        checkToCorrectClass(clazz);

        byte[] byteCode = getByteCodeOfClass(clazz);
        List<byte[]> bytecodeOfInnerClasses = getByteCodeOfInnerClasses(clazz);

        Decompiler decompiler = StateManager.getConfiguration().getDecompiler();
        Configuration configuration = StateManager.getConfiguration().getCustomDecompilerConfiguration();
        if (configuration != null) {
            decompiler.setConfiguration(configuration);
        }

        if (StateManager.getConfiguration().isSaveToFile()) {
            ByteCodeSaver saver = new ByteCodeSaver();

            saver.saveToFile(byteCode);
            for (byte[] bytecodeOfInnerClass : bytecodeOfInnerClasses) {
                saver.saveToFile(bytecodeOfInnerClass);
            }
        }

        return decompiler.decompile(byteCode, bytecodeOfInnerClasses);
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
        if (clazz.isPrimitive()) {
            throw new InvalidRetransformClass("Primitive types can not be decompiled");
        }

        if (clazz.isArray()) {
            throw new InvalidRetransformClass("Array type can not be decompiled");
        }
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        StateManager.registerConfiguration(configuration);
    }
}
