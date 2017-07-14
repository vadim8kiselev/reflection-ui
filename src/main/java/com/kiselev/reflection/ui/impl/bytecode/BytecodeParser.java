package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.DefaultByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.fernflower.FernflowerDecompiler;
import com.kiselev.reflection.ui.exception.agent.InvalidRetransformClass;

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

        Decompiler decompiler = new FernflowerDecompiler();
        decompiler.appendAdditionalClasses(collector.getByteCodeOfInnerClasses(clazz));

        return decompiler.decompile(byteCode);
    }
}
