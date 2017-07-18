package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
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

        ByteCodeSaver saver = new ByteCodeSaver();
        ByteCodeCollector collector = new DefaultByteCodeCollector();

        byte[] byteCode = collector.getByteCode(clazz);
        if (byteCode == null) {
            throw new NullPointerException("Byte code of class: " + clazz.getName() + " is not found!");
        }

        List<Class<?>> innerClasses = new ArrayList<>(InnerClassesCollector.getInnerClasses(clazz));
        List<byte[]> byteCodeOfInnerClasses = new ArrayList<>();
        for (Class<?> innerClass : innerClasses) {
            byteCodeOfInnerClasses.add(collector.getByteCode(innerClass));
        }

        Decompiler decompiler = ConfigurationManager.getDecompiler();
        decompiler.appendAdditionalClasses(byteCodeOfInnerClasses);

        saver.saveToFile(clazz, byteCode);
        for (int i = 0; i < innerClasses.size(); i++) {
            saver.saveToFile(innerClasses.get(i), byteCodeOfInnerClasses.get(i));
        }

        return decompiler.decompile(byteCode);
    }

    @Override
    public void setConfiguration(Map<String, Object> configuration) {
        ConfigurationManager.setConfiguration(configuration);
    }
}
