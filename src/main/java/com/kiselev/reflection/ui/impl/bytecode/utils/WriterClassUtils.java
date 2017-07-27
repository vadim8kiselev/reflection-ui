package com.kiselev.reflection.ui.impl.bytecode.utils;

/**
 * Created by Aleksei Makarov on 27.07.2017.
 */
public class WriterClassUtils {

    public static int getNumberOfLineSeparator(StringBuilder builder) {
        int index = builder.length() - 1;
        while (builder.charAt(index) != '\n') {
            index--;
        }

        return index + 1;
    }

    public static int getFirstNonSpaceNumber(StringBuilder builder) {
        return getFirstNonSpaceNumber(builder, builder.length());
    }

    public static int getFirstNonSpaceNumber(StringBuilder line, int number) {
        for (int i = number - 1; i > 0; i--) {
            if (line.charAt(i) != ' ') {
                return i;
            }
        }

        return -1;
    }

    public static boolean isContainsOpenBlock(CharSequence charSequence) {
        int index = charSequence.length() - 1;
        for (int i = 0; i < index; i++) {
            if (charSequence.charAt(index) == '{') {
                return true;
            }
        }

        return false;
    }

    public static String normalizeOpenBlockCharacter(StringBuilder builder) {
        int index = 1;
        while (index != 0) {
            int openBlock = builder.indexOf("{", index);
            int nonSpace = getFirstNonSpaceNumber(builder, openBlock);
            if (nonSpace != -1 && builder.charAt(nonSpace) == '\n') {
                builder.delete(nonSpace, openBlock);
                builder.insert(nonSpace, ' ');
                index = openBlock;
            } else {
                index = openBlock + 1;
            }
        }

        return builder.toString();
    }
}
