package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.configuration.Configuration;
import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.DefaultByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.configuration.ConfigurationManager;
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

        ConfigurationManager.registerConfiguration(new HashMap<>());

        ByteCodeSaver saver = new ByteCodeSaver();
        ByteCodeCollector collector = new DefaultByteCodeCollector();

        byte[] byteCode = collector.getByteCode(clazz);
        if (byteCode == null) {
            throw new NullPointerException("Byte code of class: " + clazz.getName() + " is not found!");
        }

        boolean isDecompileIC = ConfigurationManager.isDecompileInnerClasses();
        Collection<Class<?>> collection =  isDecompileIC ? InnerClassesCollector.getInnerClasses(clazz) : new ArrayList<>();

        List<Class<?>> innerClasses = new ArrayList<>(collection);
        Map<Class<?>, byte[]> byteCodeOfInnerClasses = new HashMap<>();

        for (Class<?> innerClass : innerClasses) {
            byte[] byteCodeOfInnerClass = collector.getByteCode(innerClass);
            if (byteCodeOfInnerClass != null) {
                byteCodeOfInnerClasses.put(innerClass, byteCodeOfInnerClass);
            }
        }

        Decompiler decompiler = ConfigurationManager.getDecompiler();
        Configuration configuration = ConfigurationManager.getCustomDecompilerConfiguration();
        if (configuration != null) {
            decompiler.setConfiguration(configuration);
        }
        decompiler.appendAdditionalClasses(byteCodeOfInnerClasses.values());

        if (ConfigurationManager.isSaveToFile()) {
            saver.saveToFile(clazz, byteCode);
            for (Map.Entry<Class<?>, byte[]> entry : byteCodeOfInnerClasses.entrySet()) {
                saver.saveToFile(entry.getKey(), entry.getValue());
            }
        }

        return decompiler.decompile(byteCode);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        ConfigurationManager.registerConfiguration(configuration);
    }
}
