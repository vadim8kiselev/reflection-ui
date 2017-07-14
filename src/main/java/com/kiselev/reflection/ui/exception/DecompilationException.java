package com.kiselev.reflection.ui.exception;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class DecompilationException extends ByteCodeParserException {

    public DecompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
