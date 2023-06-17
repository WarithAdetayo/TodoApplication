package org.todoapplication.appdata.entity;


import java.time.LocalDateTime;
import java.util.Objects;

public class TodoEntry {
    private String id;
    private String title;
    private String description;
    private LocalDateTime reminderDate;
    private boolean completed;
    private boolean active;
    private LocalDateTime dateCreated;
    private String ownerId;

    public TodoEntry() {}

    public TodoEntry(String title, String ownerId) {
        this.title = title;
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int hashCode() {
        if (this.id == null)
            return super.hashCode();
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        TodoEntry todoEntry = (TodoEntry) other;
        return Objects.equals(this.id, todoEntry.id);
    }
}
