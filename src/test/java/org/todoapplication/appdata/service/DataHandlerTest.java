package org.todoapplication.appdata.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.todoapplication.appdata.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {
    private static DataHandler dataHandler;

    @BeforeAll
    static void setUp() {
        dataHandler = new DataHandler("testDataFile.json");
    }

    @Test
    void addNewUserDataAndSave() {
        User user = UserService.createUser("test@email", "somePassword");
        dataHandler.addNewUserData(user);
        assertTrue(dataHandler.save());
    }

    @Test
    void loadAndGetUserData() {
        User user = UserService.createUser("user1@email", "password");
        User user2 = UserService.createUser("user2@email", "password2");

        dataHandler.addNewUserData(user);
        dataHandler.addNewUserData(user2);

        user2.setPassword("I changed the password");

        dataHandler.save();
        dataHandler.clearAllData();

        assertNull(dataHandler.getUserData("user1@email"));
        dataHandler.load();
        assertNotNull(dataHandler.getUserData("user1@email"));
    }
}
