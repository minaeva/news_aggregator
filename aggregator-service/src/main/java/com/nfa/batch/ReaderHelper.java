package com.nfa.batch;

public class ReaderHelper {

    public static String getStringNotLongerThan(String input, int length) {
        if (input.length() < length) {
            return input;
        } else {
            return input.substring(0, length - 1);
        }
    }

}
