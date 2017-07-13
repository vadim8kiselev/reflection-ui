package com.kiselev.reflection.ui.impl.bytecode.collector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 07/13/2017.
 */
public class DefaultByteCodeCollector implements ByteCodeCollector {

    List<ByteCodeCollector> collectors = new ArrayList<>();

    public DefaultByteCodeCollector() {
        collectors.add(new ClassFileByteCodeCollector());
        collectors.add(new RetransformClassByteCodeCollector());
    }

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        for (ByteCodeCollector collector : collectors) {
            byte[] byteCode = collector.getByteCode(clazz);
            if (byteCode != null) {
                return byteCode;
            }
        }

        return null;
    }

    @Override
    public List<byte[]> getByteCodeOfInnerClasses(Class<?> clazz) {
        for (ByteCodeCollector collector : collectors) {
            List<byte[]> byteCodeList = collector.getByteCodeOfInnerClasses(clazz);
            if (byteCodeList != null) {
                return byteCodeList;
            }
        }

        return null;
    }
}
