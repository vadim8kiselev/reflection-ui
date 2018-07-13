package com.classparser.bytecode.impl.collector;

import com.classparser.bytecode.api.collector.ByteCodeCollector;
import com.classparser.bytecode.impl.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class ChainByteCodeCollector implements ByteCodeCollector {

    private final ConfigurationManager configurationManager;

    public ChainByteCodeCollector(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        List<ByteCodeCollector> collectors = loadCollectors();

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

    private List<ByteCodeCollector> loadCollectors() {
        List<ByteCodeCollector> collectors = new ArrayList<>();

        ByteCodeCollector customByteCodeCollector = configurationManager.getCustomByteCodeCollector();
        if (configurationManager.isEnableCustomByteCodeCollector() && customByteCodeCollector != null) {
            collectors.add(customByteCodeCollector);
        }

        if (configurationManager.isEnableClassFileByteCodeCollector()) {
            collectors.add(new ClassFileByteCodeCollector());
        }

        if (configurationManager.isEnableRetransformClassByteCodeCollector()) {
            collectors.add(new JVMByteCodeCollector(configurationManager.getAgent()));
        }

        return collectors;
    }
}