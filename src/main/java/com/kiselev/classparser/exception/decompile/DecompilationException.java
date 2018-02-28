package com.kiselev.classparser.exception.decompile;

import com.kiselev.classparser.exception.ByteCodeParserException;

public class DecompilationException extends ByteCodeParserException {

    public DecompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
