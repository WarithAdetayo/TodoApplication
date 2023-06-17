package org.todoapplication.appdata.entity;


import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.map.HashedMap;

import java.text.Bidi;
import java.util.Map;
import java.util.Objects;

public class User {
    private String email;
    private String password;

    private Map<String, TodoEntry> todos;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, TodoEntry> getTodos() {
        return todos;
    }

    public void setTodos(Map<String, TodoEntry> todos) {
        this.todos = todos;
    }

    public void addTodo(TodoEntry todo) {
        if (this.todos == null) {
            this.todos = new HashedMap<>();
        }
        this.todos.put(todo.getId(), todo);
    }

    @Override
    public int hashCode() {
        if (this.email == null)
            return super.hashCode();
        return Objects.hashCode(this.email);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        User user = (User) other;
        return Objects.equals(this.email, user.email);
    }
}
