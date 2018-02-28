package com.kiselev.classparser.exception.file;

import com.kiselev.classparser.exception.ByteCodeParserException;

public class ReadFileException extends ByteCodeParserException {

    public ReadFileException(String message) {
        super(message);
    }

    public ReadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
