package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.impl.bytecode.holder.ByteCodeHolder;

/**
 * Created by Vadim Kiselev on 6/26/2017.
 */
public class BytecodeParser implements ReflectionUI {

    @Override
    public String parseClass(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            throw new RuntimeException("Primitive types can not be decompiled");
        }

        String decompiledByteCode = ByteCodeHolder.getDecompiledByteCode(clazz);
        return decompiledByteCode;
    }
}
