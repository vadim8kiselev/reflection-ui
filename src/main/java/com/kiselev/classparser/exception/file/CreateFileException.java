package com.kiselev.classparser.exception.file;

import com.kiselev.classparser.exception.ByteCodeParserException;

public class CreateFileException extends ByteCodeParserException {

    public CreateFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
