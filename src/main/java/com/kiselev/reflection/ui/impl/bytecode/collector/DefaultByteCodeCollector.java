package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 07/13/2017.
 */
public class DefaultByteCodeCollector implements ByteCodeCollector {

    List<ByteCodeCollector> collectors = new ArrayList<>();

    public DefaultByteCodeCollector() {
        ByteCodeCollector customByteCodeCollector = ConfigurationManager.getCustomByteCodeCollector();
        if (customByteCodeCollector != null) {
            collectors.add(customByteCodeCollector);
        }

        if (ConfigurationManager.isEnableClassFileByteCodeCollector()) {
            collectors.add(new ClassFileByteCodeCollector());
        }

        if (ConfigurationManager.isEnableRetransformClassByteCodeCollector()) {
            collectors.add(new RetransformClassByteCodeCollector());
        }
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
        if (ConfigurationManager.isDecompileInnerClasses()) {
            for (ByteCodeCollector collector : collectors) {
                List<byte[]> byteCodeList = collector.getByteCodeOfInnerClasses(clazz);
                if (!byteCodeList.isEmpty()) {
                    return byteCodeList;
                }
            }
        }

        return new ArrayList<>();
    }
}
