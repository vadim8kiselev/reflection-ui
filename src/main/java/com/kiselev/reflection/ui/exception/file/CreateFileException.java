package com.kiselev.reflection.ui.exception.file;

import com.kiselev.reflection.ui.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class CreateFileException extends ByteCodeParserException {

    public CreateFileException(String message, Throwable cause) {
        super(message, cause);
    }
}