package com.kiselev.reflection.ui.impl.bytecode;

import com.kiselev.reflection.ui.api.ReflectionUI;
import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.ClassFileByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.collector.RetransformClassByteCodeCollector;
import com.kiselev.reflection.ui.impl.bytecode.decompile.Decompiler;
import com.kiselev.reflection.ui.impl.bytecode.decompile.fernflower.FernflowerDecompiler;
import com.kiselev.reflection.ui.impl.exception.ByteCodeParserException;
import com.kiselev.reflection.ui.impl.exception.agent.InvalidRetransformClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Kiselev on 6/26/2017.
 */
public class BytecodeParser implements ReflectionUI {

    List<ByteCodeCollector> collectors = new ArrayList<>();

    {
        collectors.add(new ClassFileByteCodeCollector());
        collectors.add(new RetransformClassByteCodeCollector());
    }

    @Override
    public String parseClass(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            throw new InvalidRetransformClass("Primitive types can not be decompiled");
        }

        if (clazz.isArray()) {
            throw new InvalidRetransformClass("Array type can not be decompiled");
        }
        byte[] byteCode = null;

        Decompiler decompiler = new FernflowerDecompiler();
        for (ByteCodeCollector collector : collectors) {
            byteCode = collector.getByteCode(clazz);
            if (byteCode != null) {
                decompiler.appendAdditionalClasses(collector.getByteCodeOfInnerClasses(clazz));
                break;
            }
        }

        if (byteCode == null) {
            //I don't know what has could happen, because of which throw this exception
            throw new ByteCodeParserException("Byte code is not found o_O");
        }

        return decompiler.decompile(byteCode);
    }
}
