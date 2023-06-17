package org.todoapplication;


import org.todoapplication.UI.ActivityScreen;
import org.todoapplication.UI.Screen;
import org.todoapplication.UI.UserActivity;
import org.todoapplication.appdata.Manager;
import org.todoapplication.util.Input;
import org.todoapplication.util.Response;

import java.util.Map;

public class Main {
    private static Manager manager;
    public static void main(String[] args) {
        System.out.println("<<<<<<<<<< Todo Application >>>>>>>>>>\n");

        manager = Manager.getInstance();

        Screen currentScreen = Main::mainScreen;

        while (currentScreen != null) {
            currentScreen = currentScreen.display();
        }
    }

    public static Screen exitProgram() {
        System.out.println("Exiting program");
        manager.shutdown();
        return null;
    }

    public static Screen mainScreen() {
        System.out.println("1. Login");
        System.out.println("2. Create a new user");
        System.out.println("3. Exit Program");

        Screen[] options = {
                Main::userSignIn,
                Main::userSignUp,
                Main::exitProgram
        };

        return options[Input.acceptUserChoice(1, 3) - 1];
    }

    private static Screen userSignUp() {
        System.out.println("<<<<<<<<<< Account Sign up >>>>>>>>>>");
        String email = Input.readString("Pls enter your email: ");
        String password = Input.readMatchingString(
                2,
                new String[] {"Enter Password: ", "Confirm Password: "},
                "Password do not match"
        );

        Response<Boolean> response = manager.createUser(email, password);
        System.out.println(response.getMessage());
        return Main::mainScreen;
    }

    private static Screen userSignIn() {
        String adminUsername = Input.readString("Please enter your email: ");
        String password = Input.readString("Enter your password: ");

        Response<Boolean> response = manager.logUserIn(adminUsername, password);
        return redirect(response, Main::mainScreen, UserActivity::run);
    }

    private static Screen redirect(Response<Boolean> response, Screen nextOnInValidation, ActivityScreen nextOnValidation) {
        System.out.println(response.getMessage());
        if (response.getData()) {
            nextOnValidation.display(manager);
            return Main::mainScreen;
        } else {
            return nextOnInValidation;
        }
    }
}
