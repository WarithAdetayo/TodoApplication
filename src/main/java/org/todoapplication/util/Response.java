package org.todoapplication.util;

public class Response<T> {
    private final int statusCode;
    private final String message;
    private final T data;

    public Response(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
