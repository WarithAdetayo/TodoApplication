package org.todoapplication.util;


import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Input {
    public static void waitOnUser(String msg) {
        Scanner sc = new Scanner(System.in);
        if (msg == null)
            msg = "Press enter to continue";
        System.out.print(msg);
        sc.nextLine();
    }

    public static int acceptUserChoice(int minChoice, int maxChoice) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print(": ");

            try {
                int choice = sc.nextInt();

                if (choice >= minChoice && choice <= maxChoice ) {
                    return choice;
                } else {
                    System.out.format("Choice number between (%d - %d)\n", minChoice, maxChoice);
                }
            } catch (Exception e) {
                System.out.format("Pls enter a number from list (%d - %d)\n", minChoice, maxChoice);
                sc.nextLine();
            }
        }
    }

    public static String readString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return readString();
    }

    public static LocalDateTime readDateTimeString(String prompt) {
        prompt += "(yyyy-mm-ddThh:mm:ss): ";

        while (true) {
            String dateTimeString = readString(prompt);
            try {
                return LocalDateTime.parse(dateTimeString);
            } catch (DateTimeException e) {
                System.out.println("Invalid input, pls enter the datetime in the given format");
            }
        }
    }

    public static String readMatchingString(int noOfMatch, String[] prompts, String errorMsg) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            String firstInput = readString(prompts[0]);
            boolean mismatch = false;

            for (int i = 1; i < noOfMatch; i++) {
                String sInput = readString(prompts[i]);
                if (!firstInput.equals(sInput)) {
                    System.out.println(errorMsg);
                    mismatch = true;
                }
            }
            if (mismatch) continue;
            return firstInput;
        }
    }
}
