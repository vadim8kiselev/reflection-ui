package com.classparser.bytecode.impl.utils;

import com.classparser.exception.ByteCodeParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    private static final int BYTE_BUFFER_SIZE = 1024;

    public static byte[] readBytesFromInputStream(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int batchSize;
        byte[] data = new byte[BYTE_BUFFER_SIZE];
        try {
            batchSize = stream.read(data, 0, data.length);
            while (batchSize != -1) {
                buffer.write(data, 0, batchSize);
                batchSize = stream.read(data, 0, data.length);
            }

            buffer.flush();
        } catch (IOException exception) {
            throw new ByteCodeParserException("Occurred problems at class loading!", exception);
        }

        return buffer.toByteArray();
    }
}
