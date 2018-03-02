package com.classparser.exception.decompile;

import com.classparser.exception.ByteCodeParserException;

public class DecompilationException extends ByteCodeParserException {

    public DecompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
