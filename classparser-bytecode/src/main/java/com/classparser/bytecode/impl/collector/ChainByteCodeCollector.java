package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.configuration.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ChainByteCodeCollector implements ByteCodeCollector {

    private List<ByteCodeCollector> collectors = new ArrayList<>();

    public ChainByteCodeCollector() {
        ByteCodeCollector customByteCodeCollector = StateManager.getConfiguration().getCustomByteCodeCollector();
        if (StateManager.getConfiguration().isEnableCustomByteCodeCollector() && customByteCodeCollector != null) {
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
        if (clazz != null) {
            for (ByteCodeCollector collector : collectors) {
                byte[] byteCode = collector.getByteCode(clazz);

                if (byteCode != null) {
                    return byteCode;
                }
            }
        }

        return null;
    }
}
