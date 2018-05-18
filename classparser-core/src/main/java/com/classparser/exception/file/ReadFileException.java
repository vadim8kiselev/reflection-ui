package com.classparser.exception.file;

import com.classparser.exception.ByteCodeParserException;

public class ReadFileException extends ByteCodeParserException {

    public ReadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
