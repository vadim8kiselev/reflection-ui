package com.classparser.test;

import com.classparser.api.ClassParser;
import com.classparser.bytecode.impl.BytecodeParser;
import com.classparser.bytecode.impl.assembly.attach.ClassDefiner;
import com.classparser.bytecode.impl.assembly.attach.ResourceLoader;
import com.classparser.bytecode.impl.configuration.ByteCodeBuilderConfiguration;
import com.classparser.bytecode.impl.decompile.cfr.CFRDecompiler;
import com.classparser.bytecode.impl.utils.ClassFileUtils;
import com.classparser.bytecode.impl.utils.IOUtils;
import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        List<Runnable> runnable = new ArrayList<>();



        /*long time = System.currentTimeMillis();

        ClassParser parser = new BytecodeParser();
        Map<String, Object> configuration = ByteCodeBuilderConfiguration
                .configure()
                .enableClassFileByteCodeCollector(false)
                .setDecompiler(new CFRDecompiler())
                .getConfiguration();
        parser.setConfiguration(configuration);
        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        System.out.println(parser.parseClass(Class.class));
        System.out.println(System.currentTimeMillis() - time);*/
    }

    private static class Tester implements Runnable {

        private final ClassParser classParser;

        public Tester(ClassParser classParser) {
            this.classParser = classParser;
        }

        @Override
        public void run() {
        }
    }
}
