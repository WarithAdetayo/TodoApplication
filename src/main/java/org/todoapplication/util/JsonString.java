package org.todoapplication.util;

import com.google.gson.Gson;

public class JsonString {
    public static <T> String toJsonString(T obj) {
        return (new Gson()).toJson(obj);
    }

    public static <T> T toObject(String jsonStringValue, Class<?> cls) {
        return (T)(new Gson()).fromJson(jsonStringValue, cls);
    }
}
