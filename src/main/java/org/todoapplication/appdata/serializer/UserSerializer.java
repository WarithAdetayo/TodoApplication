package org.todoapplication.appdata.serializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.todoapplication.appdata.entity.TodoEntry;
import org.todoapplication.appdata.entity.User;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class UserSerializer implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", user.getEmail());
        jsonObject.addProperty("password", user.getPassword());
        jsonObject.add("todoList", jsonSerializationContext.serialize(user.getTodos()));

        return jsonObject;
    }

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String email = jsonObject.get("email").getAsString();
        String password = jsonObject.get("password").getAsString();
        Map<String, TodoEntry> todoList = jsonDeserializationContext.deserialize(
                jsonObject.get("todoList"),
                new TypeToken<Map<String, TodoEntry>>() {}.getType()
        );

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setTodos(todoList);
        return user;
    }
}
