package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.DefaultByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.saver.ByteCodeSaver;
import com.kiselev.reflection.ui.impl.bytecode.utils.InnerClassesCollector;

import java.util.*;

/**
 * Created by Vadim Kiselev on 6/26/2017.
 */
public class BytecodeParser implements ReflectionUI {

    @Override
    public String parseClass(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            throw new InvalidRetransformClass("Primitive types can not be decompiled");
        }

        if (clazz.isArray()) {
            throw new InvalidRetransformClass("Array type can not be decompiled");
        }

        ByteCodeCollector collector = new DefaultByteCodeCollector();

        byte[] byteCode = collector.getByteCode(clazz);
        if (byteCode == null) {
            throw new NullPointerException(String.format("Byte code of class: %s is not found!", clazz.getName()));
        }

        boolean isDecompileIC = StateManager.getConfiguration().isDecompileInnerClasses();

        Collection<Class<?>> collection =  isDecompileIC ? InnerClassesCollector.getInnerClasses(clazz) : new ArrayList<>();
        List<byte[]> bytecodeOfInnerClasses = new ArrayList<>();

        for (Class<?> innerClass : collection) {
            byte[] byteCodeOfInnerClass = collector.getByteCode(innerClass);
            if (byteCodeOfInnerClass != null) {
                bytecodeOfInnerClasses.add(byteCodeOfInnerClass);
            }
        }

        Decompiler decompiler = StateManager.getConfiguration().getDecompiler();
        Configuration configuration = StateManager.getConfiguration().getCustomDecompilerConfiguration();
        if (configuration != null) {
            decompiler.setConfiguration(configuration);
        }
        decompiler.appendAdditionalClasses(bytecodeOfInnerClasses);

        if (StateManager.getConfiguration().isSaveToFile()) {
            ByteCodeSaver saver = new ByteCodeSaver();

            saver.saveToFile(byteCode);
            for (byte[] bytecodeOfInnerClass : bytecodeOfInnerClasses) {
                saver.saveToFile(bytecodeOfInnerClass);
            }
        }

        return decompiler.decompile(byteCode);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        StateManager.registerConfiguration(configuration);
    }
}
