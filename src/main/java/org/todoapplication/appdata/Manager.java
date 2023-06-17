package org.todoapplication.appdata;

import org.todoapplication.appdata.DTO.TodoEntryDTO;
import org.todoapplication.appdata.DTO.UserDTO;
import org.todoapplication.appdata.entity.TodoEntry;
import org.todoapplication.appdata.entity.User;
import org.todoapplication.appdata.service.DataHandler;
import org.todoapplication.appdata.service.TodoEntryService;
import org.todoapplication.appdata.service.UserService;
import org.todoapplication.util.Response;
import org.todoapplication.util.ResponseManager;

import java.time.LocalDateTime;
import java.util.List;


public class Manager {
    private List<User> users;
    private List<TodoEntry> todos;
    private User userLoggedIn;
    private final DataHandler dataHandler;
    private static Manager manager;

    private Manager() {
        dataHandler = new DataHandler("appdata.json");
    }

    public static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }

    public Response<Boolean> createUser(String email, String password) {
        if (dataHandler.emailExists(email))
            return ResponseManager.badRequestMessage("User exists already", false);
        User user = UserService.createUser(email, password);
        dataHandler.addNewUserData(user);
        return ResponseManager.createdMessage("Account Created Successfully", true);
    }

    public Response<Boolean> logUserIn(String email, String password) {
        if (dataHandler.emailExists(email)) {
            User user = dataHandler.getUserData(email);
            if (UserService.checkPassword(user, password)) {
                this.userLoggedIn = user;
                return ResponseManager.okMessage("Login Successfully", true);
            }
        }
        return ResponseManager.notAuthorisedMessage("Invalid Login Credentials", false);
    }

    public Response<Boolean> signOutUser() {
        if (userIsSignedIn()) {
            this.userLoggedIn = null;
            return ResponseManager.okMessage("User Signed out successfully", true);
        }
        return ResponseManager.badRequestMessage("User not signed in", false);
    }

    public Response<Boolean> changePassword(String oldPassword, String newPassword) {
        if (!userIsSignedIn()) {
            return ResponseManager.notSignedInMessage(false);
        }

        if (!UserService.checkPassword(this.userLoggedIn, oldPassword)) {
            return ResponseManager.notAuthorisedMessage("Incorrect old password", false);
        }

        UserService.changePassword(this.userLoggedIn, newPassword);
        return ResponseManager.okMessage("Password Changed Successfully", true);
    }
    public Response<Boolean> createTodo(String title, String description, LocalDateTime reminder) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", false);
        TodoEntry todo = TodoEntryService.createTodo(title, this.userLoggedIn.getEmail());
        TodoEntryService.setTodoReminder(todo, reminder);
        TodoEntryService.setTodoDescription(todo, description);
        this.userLoggedIn.addTodo(todo);
        return ResponseManager.createdMessage("Todo Created Successfully", true);
    }

    public Response<List<TodoEntryDTO>> getAllTodo() {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);
        return ResponseManager.okMessage(
                TodoEntryService.transform(
                        this.userLoggedIn.getTodos().values()
                )
        );
    }

    public Response<List<TodoEntryDTO>> findTodoByTitle(String title) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.selectTodosByTitle(
                        this.userLoggedIn.getTodos().values(),
                        title
                )
        );
    }

    public Response<List<TodoEntryDTO>> findTodoByKeywordInDescription(String keyword) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.searchTodosByKeywordInDescription(
                        this.userLoggedIn.getTodos().values(),
                        keyword
                )
        );
    }

    public Response<List<TodoEntryDTO>> getAllInCompletedTodo() {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.selectAllUnCompletedTodos(
                        this.userLoggedIn.getTodos().values()
                )
        );
    }

    public Response<List<TodoEntryDTO>> getAllActiveTodo() {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.selectAllActiveTodos(
                        this.userLoggedIn.getTodos().values()
                )
        );
    }

    public Response<List<TodoEntryDTO>> getTodoCreatedAt(LocalDateTime date) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.selectTodosByDate(
                        this.userLoggedIn.getTodos().values(),
                        date
                )
        );
    }

    public Response<List<TodoEntryDTO>> getTodoWithRemainderAt(LocalDateTime date) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);

        return ResponseManager.okMessage(
                TodoEntryService.selectTodosByReminderDate(
                        this.userLoggedIn.getTodos().values(),
                        date
                )
        );
    }

    public Response<Boolean> deleteTodo(String todoId) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", false);
        if (this.userLoggedIn.getTodos().containsKey(todoId)) {
            this.userLoggedIn.getTodos().remove(todoId);
            return ResponseManager.okMessage("Todo Deleted", true);
        }
        return ResponseManager.badRequestMessage("Todo not found", false);
    }

    public Response<Boolean> updateTodoFromDTO(TodoEntryDTO todoEntryDTO) {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", false);
        TodoEntry todo = this.userLoggedIn.getTodos().get(todoEntryDTO.getId());
        if (todo != null){
            TodoEntryService.updateTodoFromDTO(todo, todoEntryDTO);
            return ResponseManager.okMessage("Changes Updated", true);
        }
        return ResponseManager.badRequestMessage("Todo not found", false);
    }

    public Response<UserDTO> getUserInformation() {
        if (!userIsSignedIn())
            return ResponseManager.notAuthorisedMessage("User is not Signed In", null);
        return ResponseManager.okMessage(UserService.toUserDTO(this.userLoggedIn));
    }

    public boolean userIsSignedIn() {
        return (this.userLoggedIn != null);
    }

    public void shutdown() {
        dataHandler.save();
    }
}
