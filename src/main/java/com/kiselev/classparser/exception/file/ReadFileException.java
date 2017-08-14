package com.kiselev.classparser.exception.file;

import com.kiselev.classparser.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class ReadFileException extends ByteCodeParserException {

    public ReadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
