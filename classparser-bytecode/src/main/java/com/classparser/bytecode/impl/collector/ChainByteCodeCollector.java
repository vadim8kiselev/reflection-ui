package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.configuration.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ChainByteCodeCollector implements ByteCodeCollector {

    private final List<ByteCodeCollector> collectors = new ArrayList<>();

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        loadCollectors();
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

    private void loadCollectors() {
        collectors.clear();

        ByteCodeCollector customByteCodeCollector = StateManager.getConfiguration().getCustomByteCodeCollector();
        if (StateManager.getConfiguration().isEnableCustomByteCodeCollector() && customByteCodeCollector != null) {
            collectors.add(customByteCodeCollector);
        }

        System.out.println(StateManager.getConfiguration().isEnableClassFileByteCodeCollector());
        if (StateManager.getConfiguration().isEnableClassFileByteCodeCollector()) {
            collectors.add(new ClassFileByteCodeCollector());
        }

        if (StateManager.getConfiguration().isEnableRetransformClassByteCodeCollector()) {
            collectors.add(new FromJVMByteCodeCollector());
        }
    }
}
