package com.kiselev.reflection.ui.impl.bytecode.collector;

import com.kiselev.reflection.ui.impl.bytecode.agent.JavaAgent;
import com.kiselev.reflection.ui.impl.bytecode.configuration.StateManager;

/**
 * Created by Aleksei Makarov on 07/12/2017.
 */
public class FromJVMByteCodeCollector implements ByteCodeCollector {

    private static JavaAgent agent = StateManager.getConfiguration().getAgent();

    @Override
    public byte[] getByteCode(Class<?> clazz) {
        return agent.getByteCode(clazz);
    }
}
