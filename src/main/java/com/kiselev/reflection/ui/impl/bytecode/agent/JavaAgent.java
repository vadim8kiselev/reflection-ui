package com.kiselev.reflection.ui.impl.bytecode.agent;

import com.kiselev.reflection.ui.impl.bytecode.collector.ByteCodeCollector;

import java.lang.instrument.Instrumentation;

/**
 * Created by Aleksei Makarov on 08/03/2017.
 */
public interface JavaAgent extends ByteCodeCollector {

    Instrumentation getInstrumentation();
}
