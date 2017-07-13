package com.kiselev.reflection.ui.impl.exception;

/**
 * Created by Aleksei Makarov on 13.07.2017.
 */
public class ByteCodeParserException  extends RuntimeException {

    public ByteCodeParserException(String message) {
        super(message);
    }

    public ByteCodeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
