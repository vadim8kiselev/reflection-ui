package com.kiselev.reflection.ui.exception.agent;

import com.kiselev.reflection.ui.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/28/2017.
 */
public class ClassNotFoundException extends ByteCodeParserException {

    public ClassNotFoundException(String message) {
        super(message);
    }
}
