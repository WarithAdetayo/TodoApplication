package org.todoapplication.appdata.serializer;

import com.google.gson.*;
import org.todoapplication.appdata.entity.TodoEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class TodoEntrySerializer implements JsonSerializer<TodoEntry>, JsonDeserializer<TodoEntry> {

    @Override
    public JsonElement serialize(TodoEntry todoEntry, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        for (Field f : todoEntry.getClass().getFields()) {
            try {
                jsonObject.addProperty(f.getName(), (String) f.get(todoEntry));
            } catch (IllegalAccessException e) {
                continue;
            }
        }

        return jsonObject;
    }

    @Override
    public TodoEntry deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
