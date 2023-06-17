package org.todoapplication.appdata.service;

import org.apache.commons.collections4.map.HashedMap;
import org.todoapplication.appdata.DTO.UserDTO;
import org.todoapplication.appdata.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {

    public static User createUser(String email, String password) {
        User user = new User(email, hashPassword(password));
        user.setTodos(new HashedMap<>());
        return user;
    }

    public static void changePassword(User user, String newPassword) {
        user.setPassword(hashPassword(newPassword));
    }

    public static boolean checkPassword(User user, String password) {
        return user.getPassword().equals(hashPassword(password));
    }

    private static String hashPassword(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getEmail());
    }
}
