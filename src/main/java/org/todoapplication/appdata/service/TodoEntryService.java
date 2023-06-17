package org.todoapplication.appdata.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.todoapplication.appdata.DTO.TodoEntryDTO;
import org.todoapplication.appdata.entity.TodoEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoEntryService {

    public static TodoEntry createTodo(String title, String userId) {
        TodoEntry todo = new TodoEntry(title, userId);
        todo.setId(UUID.randomUUID().toString());
        todo.setDateCreated(LocalDateTime.now());
        todo.setActive(true);
        todo.setCompleted(false);
        return todo;
    }

    public static void setTodoActive(TodoEntry todoEntry, boolean active) {
        todoEntry.setActive(active);
    }
    public static void setTodoReminder(TodoEntry todoEntry, LocalDateTime date) {
        todoEntry.setReminderDate(date);
    }

    public static void markTodoCompleted(TodoEntry todoEntry) {
        todoEntry.setCompleted(true);
    }

    public static void setTodoDescription(TodoEntry todoEntry, String description) {
        todoEntry.setDescription(description);
    }

    public static List<TodoEntryDTO> selectTodosByDate(Iterable<TodoEntry> todoEntries, LocalDateTime date) {

        return transform(CollectionUtils.select(
                todoEntries,
                todoEntry -> {
                    return compareDate(todoEntry.getDateCreated(), date);
                }
        ));
    }

    public static List<TodoEntryDTO> selectTodosByReminderDate(Iterable<TodoEntry> todoEntries, LocalDateTime date) {
        return transform(CollectionUtils.select(
                todoEntries,
                todoEntry -> {
                    return compareDate(todoEntry.getReminderDate(), date);
                }
        ));
    }

    public static List<TodoEntryDTO> selectTodosByTitle(Iterable<TodoEntry> todoEntries, String searchPattern) {
        return transform(CollectionUtils.select(
                todoEntries,
                todoEntry -> {
                    return keywordMatches(searchPattern, todoEntry.getTitle());
                }
        ));
    }

    public static List<TodoEntryDTO> searchTodosByKeywordInDescription(Iterable<TodoEntry> todoEntries, String searchPattern) {
        return transform(CollectionUtils.select(
                todoEntries,
                todoEntry -> {
                    return keywordMatches(searchPattern, todoEntry.getDescription());
                }
        ));
    }

    public static List<TodoEntryDTO> selectAllCompletedTodos(Iterable<TodoEntry> todoEntries) {
        return transform(CollectionUtils.select(
                todoEntries,
                TodoEntry::isCompleted
        ));
    }

    public static List<TodoEntryDTO> selectAllUnCompletedTodos(Iterable<TodoEntry> todoEntries) {
        return transform(CollectionUtils.select(
                todoEntries,
                (TodoEntry todoEntry) -> !todoEntry.isCompleted()
        ));
    }

    public static List<TodoEntryDTO> selectAllActiveTodos(Iterable<TodoEntry> todoEntries) {
        return transform(CollectionUtils.select(
                todoEntries,
                TodoEntry::isActive
        ));
    }

    public static void updateTodoFromDTO(TodoEntry todo, TodoEntryDTO todoEntryDTO) {
        todo.setTitle(todoEntryDTO.getTitle());
        todo.setReminderDate(todoEntryDTO.getReminderDate());
        todo.setDescription(todoEntryDTO.getDescription());
        todo.setActive(todoEntryDTO.isActive());
        todo.setCompleted(todoEntryDTO.isCompleted());
    }

    public static boolean compareDate(LocalDateTime date1, LocalDateTime date2) {
        if (date1.getYear() != date2.getYear())
            return false;
        if (date1.getMonth() != date2.getMonth())
            return false;
        return date1.getDayOfMonth() == date2.getDayOfMonth();
    };

    public static boolean keywordMatches(String keyword, String text) {
        Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static TodoEntryDTO convertTodoToDTO(TodoEntry todo) {
        TodoEntryDTO todoDTO = new TodoEntryDTO();
        todoDTO.setId(todo.getId());
        todoDTO.setTitle(todo.getTitle());
        todoDTO.setDescription(todo.getDescription());
        todoDTO.setOwnerId(todo.getOwnerId());
        todoDTO.setCompleted(todo.isCompleted());
        todoDTO.setActive(todo.isActive());
        todoDTO.setDateCreated(todo.getDateCreated());
        todoDTO.setReminderDate(todo.getReminderDate());
        return todoDTO;
    }
    
    public static List<TodoEntryDTO> transform(Iterable<TodoEntry> todoEntries) {
        return (List<TodoEntryDTO>)CollectionUtils.collect(
                todoEntries,
                new Transformer<TodoEntry, TodoEntryDTO>() {
                    @Override
                    public TodoEntryDTO transform(TodoEntry todoEntry) {
                        return convertTodoToDTO(todoEntry);
                    }
                }
        );
    }
}
