package org.todoapplication.util;

public class Output {
    public static void printNumberedOption(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.format("%d. %s\n", i + 1, options[i]);
        }
    }
}
