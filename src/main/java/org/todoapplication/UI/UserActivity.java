package org.todoapplication.UI;

import org.todoapplication.appdata.DTO.TodoEntryDTO;
import org.todoapplication.appdata.DTO.UserDTO;
import org.todoapplication.appdata.Manager;
import org.todoapplication.util.Input;
import org.todoapplication.util.Output;
import org.todoapplication.util.Response;

import java.time.LocalDateTime;
import java.util.List;

public class UserActivity {
    private static Manager manager;
    public static void run(Manager manager) {
        UserActivity.manager = manager;

        Screen currentScreen = UserActivity::homeScreen;

        while (currentScreen != null) {
            currentScreen = currentScreen.display();
        }
    }

    public static Screen homeScreen() {
        String email = manager.getUserInformation().getData().email();
        System.out.println("Welcome back " + email);
        String[] options = {
                "Create a new Todo Entry",
                "View Todo Entries",
                "Update Profile",
                "Logout"
        };

        Screen[] mappedActivity = {
                UserActivity::createTodo,
                UserActivity::viewTodo,
                UserActivity::updateProfile,
                UserActivity::logout
        };

        Output.printNumberedOption(options);
        return mappedActivity[Input.acceptUserChoice(1, options.length) - 1];
    }

    public static Screen logout() {
        System.out.println((manager.signOutUser().getMessage()));
        return null;
    }

    public static Screen createTodo() {
        String title = Input.readString("Enter the name/title of the todo: ");
        String description = Input.readString("Pls describe the todo: ");
        LocalDateTime reminder = Input.readDateTimeString("Pls enter reminder Date and Time");
        Response<Boolean> response = manager.createTodo(title, description, reminder);
        System.out.println(response.getMessage());
        Input.waitOnUser("\nPress enter to continue to home screen");
        return UserActivity::homeScreen;
    }

    public static Screen viewTodo() {
        String[] options = {
                "View all Todos",
                "View Active Todos",
                "View Uncompleted Todos",
                "Search Todo by Title",
                "Search Todo by Description",
                "Return Back to Home Screen"
        };
        Output.printNumberedOption(options);
        int choice = Input.acceptUserChoice(1, options.length);
        if (choice == options.length)
            return UserActivity::homeScreen;
        List<TodoEntryDTO> todoEntryDTOS;
        switch (choice) {
            case 1 -> todoEntryDTOS = manager.getAllTodo().getData();
            case 2 -> todoEntryDTOS = manager.getAllActiveTodo().getData();
            case 3 -> todoEntryDTOS = manager.getAllInCompletedTodo().getData();
            case 4 -> {
                String title = Input.readString(
                        "Enter the title of the Todo Entry you want to search for: "
                );
                todoEntryDTOS = manager.findTodoByTitle(title).getData();
            }
            case 5 -> {
                String keyword = Input.readString(
                        "Enter the title of the Todo Entry you want to search for: "
                );
                todoEntryDTOS = manager.findTodoByKeywordInDescription(keyword).getData();
            }
            default -> {
                return UserActivity::homeScreen;
            }
        }
        return displayTodo(todoEntryDTOS);
    }

    public static Screen displayTodo(List<TodoEntryDTO> todoEntries) {
        if (todoEntries == null || todoEntries.size() == 0) {
            System.out.println("No Todo found");
            Input.waitOnUser("No Todo found, Return to the HomeScreen to create a Todo");
            return UserActivity::homeScreen;
        }

        System.out.println("Todo:");
        for (int i = 0; i < todoEntries.size(); i++) {
            System.out.format(
                    "\t%d. %s (Created: %s)\n",
                    i + 1,
                    todoEntries.get(i).getTitle(),
                    todoEntries.get(i).getDateCreated()
            );
        }
        System.out.println((todoEntries.size() + 1) + ". Return back");

        int choice = Input.acceptUserChoice(1, todoEntries.size() + 1);

        if (choice < todoEntries.size() + 1)
            todoScreen(todoEntries.get(choice - 1));

        return UserActivity::viewTodo;
    }
    public static void todoScreen(TodoEntryDTO todoEntry) {
        // Display the todo here
        System.out.println("<<<<<<<<<< Todo >>>>>>>>>>");
        System.out.println("\tTitle: " + todoEntry.getTitle());
        System.out.println("\tDescription: " + todoEntry.getDescription());
        System.out.println("\tDate Created: " + todoEntry.getDateCreated());
        System.out.println("\tReminder At: " + todoEntry.getReminderDate() + (
                (todoEntry.getReminderDate().isBefore(LocalDateTime.now())) ?
                        " (Reminder Passed!!!)" : ""
                ));
        System.out.println("\tActive: " + todoEntry.isActive());
        System.out.println("\tCompleted: " + todoEntry.isCompleted() + "\n");

        String[] options = {
            todoEntry.isCompleted() ? "Mark Uncompleted" : "Mark Completed",
            todoEntry.isActive() ? "Set Inactive" : "Set Active",
            "Update Description",
            "Change Reminder Date",
            "Save Changes",
            "Delete Todo",
            "Return Back"
        };
        Output.printNumberedOption(options);
        int choice = Input.acceptUserChoice(1, options.length);
        switch (choice) {
            case 1 -> todoEntry.setCompleted(!todoEntry.isCompleted());
            case 2 -> todoEntry.setActive(!todoEntry.isActive());
            case 3 -> {
                String description = Input.readString("Enter New Description: ");
                todoEntry.setDescription(description);
            }
            case 4 -> {
                LocalDateTime date = Input.readDateTimeString("Enter New Reminder Date");
                todoEntry.setReminderDate(date);
            }
            case 5 -> {
                System.out.println(manager.updateTodoFromDTO(todoEntry).getMessage());
                Input.waitOnUser("Press Enter to continue");
            }
            case 6 -> {
                System.out.println("Are you sure you want to Delete\n1. Yes\nNo");
                if (Input.acceptUserChoice(1, 2) == 1) {
                    System.out.println(manager.deleteTodo(todoEntry.getId()).getMessage());
                    return;
                }
            }
            case 7 -> {
                return;
            }
        }
        todoScreen(todoEntry);
    }

    public static Screen updateProfile() {
        System.out.println("<<<<<<<<<< User Profile >>>>>>>>>>");
        UserDTO userData = manager.getUserInformation().getData();
        System.out.println("\n\t\tYour Email: " + userData.email() + "\n");

        Output.printNumberedOption(new String[] {
                "Change Password",
                "Return Back to Home Screen"
        });

        int choice = Input.acceptUserChoice(1, 2);

        if (choice == 1) {
            String oldPassword = Input.readString("Input Old Password: ");
            String newPassword = Input.readMatchingString(
                    2,
                    new String[] {"Input new password: ", "Confirm Password: "},
                    "Password Mismatch"
            );

            System.out.println(manager.changePassword(oldPassword, newPassword).getMessage());
            return UserActivity::updateProfile;
        }
        return UserActivity::homeScreen;
    }
}
