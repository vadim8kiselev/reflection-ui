package com.kiselev.reflection.ui.impl.exception;

/**
 * Created by Aleksei Makarov on 13.07.2017.
 */
public class DecompilationException extends ByteCodeParserException {

    public DecompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
