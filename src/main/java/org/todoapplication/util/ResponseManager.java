package org.todoapplication.util;

public class ResponseManager {
    public static <M> Response<M> notSignedInMessage(M data) {
        return new Response<>(401, "User not signed in", data);
    }
    public static <M> Response<M> notAuthorisedMessage(M data) {
        return new Response<>(401, "Not Authorised", data);
    }

    public static <M> Response<M> notAuthorisedMessage(String message, M data) {
        return new Response<>(401, message, data);
    }
    public static <M> Response<M> okMessage(M data) {
        return new Response<>(200, "Request Ok!", data);
    }
    public static <M> Response<M> okMessage(String message, M data) {
        return new Response<>(200, message, data);
    }
    public static <M> Response<M> badRequestMessage(M data) {
        return new Response<>(400, "Bad Request!", data);
    }
    public static <M> Response<M> badRequestMessage(String message, M data) {
        return new Response<>(400, message, data);
    }
    public static <M> Response<M> internalServerErrorMessage(M data) {
        return new Response<>(500, "Internal Server Error", data);
    }
    public static <M> Response<M> internalServerErrorMessage(String message, M data) {
        return new Response<>(500, message, data);
    }
    public static <M> Response<M> createdMessage(M data) {
        return new Response<>(201, "Created!", data);
    }
    public static <M> Response<M> createdMessage(String message, M data) {
        return new Response<>(201, message, data);
    }

}
