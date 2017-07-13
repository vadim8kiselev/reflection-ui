package com.kiselev.reflection.ui.impl.exception.agent;

import com.kiselev.reflection.ui.impl.exception.ByteCodeParserException;

/**
 * Created by Aleksei Makarov on 07/13/2017.
 */
public class AgentAttachException extends ByteCodeParserException {

    public AgentAttachException(String message, Throwable cause) {
        super(message, cause);
    }
}
