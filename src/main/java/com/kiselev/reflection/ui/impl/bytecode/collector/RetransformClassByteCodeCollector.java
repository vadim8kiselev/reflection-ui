package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;
import com.kiselev.reflection.ui.impl.bytecode.utils.InnerClassesCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class RetransformClassByteCodeCollector implements ByteCodeCollector {

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        return ByteCodeHolder.getByteCode(clazz);
    }

    @Override
    public List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz) {
        List<byte[]> innerClasses = new ArrayList<>();

        for (Class<?> innerClass : InnerClassesCollector.getInnerClasses(clazz)) {
            innerClasses.add(ByteCodeHolder.getByteCode(innerClass));
        }

        return innerClasses;
    }
}
