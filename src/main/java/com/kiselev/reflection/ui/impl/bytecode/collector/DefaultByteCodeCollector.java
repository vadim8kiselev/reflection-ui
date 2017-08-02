package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 07/13/2017.
 */
public class DefaultByteCodeCollector implements ByteCodeCollector {

    List<ByteCodeCollector> collectors = new ArrayList<>();

    public DefaultByteCodeCollector() {
        ByteCodeCollector customByteCodeCollector = StateManager.getConfiguration().getCustomByteCodeCollector();
        if (customByteCodeCollector != null && StateManager.getConfiguration().isEnableCustomByteCodeCollector()) {
            collectors.add(customByteCodeCollector);
        }

        if (StateManager.getConfiguration().isEnableClassFileByteCodeCollector()) {
            collectors.add(new ClassFileByteCodeCollector());
        }

        if (StateManager.getConfiguration().isEnableRetransformClassByteCodeCollector()) {
            collectors.add(new FromJVMByteCodeCollector());
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
}
