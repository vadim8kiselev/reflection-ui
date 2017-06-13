package com.kiselev.reflection.ui.bytecode.assembly.build.action;

public interface Action<T> {

    boolean isActionComplete(T data);
}
