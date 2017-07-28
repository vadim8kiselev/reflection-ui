package com.kiselev.reflection.ui.impl.bytecode.utils;

/**
 * Created by Aleksei Makarov on 07/27/2017.
 */
public class ClassStringUtils {

    private static final Character OPEN_BLOCK = '{';

    private static final Character LINE_SEPARATOR = '\n';

    public static int getNumberOfLineSeparator(StringBuilder builder) {
        int index = builder.length() - 1;
        while (builder.charAt(index) != LINE_SEPARATOR) {
            index--;
        }

        return index + 1;
    }

    public static int getFirstNonSpaceNumber(StringBuilder builder) {
        return getFirstNonSpaceNumber(builder, builder.length());
    }

    public static int getFirstNonSpaceNumber(StringBuilder line, int number) {
        for (int i = number - 1; i > 0; i--) {
            if (line.charAt(i) != OPEN_BLOCK) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isContainsOpenBlock(CharSequence charSequence) {
        int index = charSequence.length() - 1;
        for (int i = 0; i < index; i++) {
            if (charSequence.charAt(index) == OPEN_BLOCK) {
                return true;
            }
        }

        return false;
    }

    public static String normalizeOpenBlockCharacter(StringBuilder builder) {
        int index = 1;
        while (index != 0) {
            int openBlock = builder.indexOf(OPEN_BLOCK.toString(), index);
            int nonSpace = getFirstNonSpaceNumber(builder, openBlock);
            if (nonSpace != -1 && builder.charAt(nonSpace) == LINE_SEPARATOR) {
                builder.delete(nonSpace, openBlock);
                builder.insert(nonSpace, ' ');
                index = openBlock;
            } else {
                index = openBlock + 1;
            }
        }

        return builder.toString();
    }

    public static boolean isEmpty(String line) {
        return line != null && !line.isEmpty();
    }

    public static String delete(String from, String deleteElement) {
        return from.replace(deleteElement, "");
    }
}
