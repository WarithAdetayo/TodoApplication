package org.todoapplication.appdata.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.collections4.map.HashedMap;
import org.todoapplication.appdata.entity.User;
import org.todoapplication.appdata.serializer.LocalDateTimeAdapter;
import org.todoapplication.appdata.serializer.UserSerializer;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;


public class DataHandler {
    private Map<String, User> users;
    private final Gson gson;
    private final String filePath;
    private final Logger logger;


    public DataHandler(String filePath) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(User.class, new UserSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        this.filePath = filePath;
        this.logger = LogManager.getLogger(DataHandler.class);
        this.load();
    }

    public void load() {
        try (FileReader reader = new FileReader(this.filePath)) {
            this.users = gson.fromJson(
                    reader,
                    new TypeToken<Map<String, User>>() {}.getType()
            );
            if (this.users != null){
                logger.info("File read successful");
                return;
            }
        } catch (FileNotFoundException e) {
            logger.warn("File doesn't exist");
        } catch (IOException e) {
            logger.warn("Error reading file");
            throw new RuntimeException(e);
        }
        this.users = new HashedMap<String, User>();
    }

    public boolean save() {
        try (FileWriter writer = new FileWriter(this.filePath)) {
            gson.toJson(this.users, writer);
            logger.info("File write successful");
            return true;
        } catch (IOException e) {
            logger.error("Error writing data to file");
            return false;
        }
    }

    public User getUserData(String email) {
        return users.get(email);
    }

    public boolean emailExists(String email) {
        return users.containsKey(email);
    }

    public void addNewUserData(User user) {
        this.users.put(user.getEmail(), user);
    }

    public void clearAllData() {
        this.users.clear();
    }
}
