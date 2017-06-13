package com.kiselev.reflection.ui.bytecode.assembly.build.action;

import java.io.File;

public class FileDeleter {

    public static <T> void deleteAfterCreateFile(String pathDeletableFile, Action<T> action, T data) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!action.isActionComplete(data));
                File file = new File(pathDeletableFile);
                while (!file.delete());
            }
        });

        thread.start();
    }
}
