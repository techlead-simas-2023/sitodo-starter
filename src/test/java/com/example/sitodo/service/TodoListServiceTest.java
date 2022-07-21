package com.example.sitodo.service;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Tag("unit")
class TodoListServiceTest {

    private TodoListService todoListService;

    @Mock
    private TodoListRepository todoListRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        todoListService = new TodoListService();
        todoListService.setTodoListRepository(todoListRepository);
    }

    @Test
    @DisplayName("Given an existing ID, getTodoListById returns an existing list")
    void getTodoListById_ok() {
        TodoItem todoItem = new TodoItem("Buy milk");
        TodoList todoList = new TodoList(List.of(todoItem));
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        TodoList savedList = todoListService.getTodoListById(1L);

        assertFalse(savedList.getItems().isEmpty());
    }

    @Test
    @DisplayName("Given a new todo item, addTodoItem saves the item into a new list")
    void addTodoItem_ok() {
        TodoItem todoItem = new TodoItem("Buy milk");
        when(todoListRepository.save(any(TodoList.class))).thenReturn(new TodoList(List.of(todoItem)));

        TodoList savedList = todoListService.addTodoItem(todoItem);
        TodoItem savedTodoItem = savedList.getItems().get(0);

        assertFalse(savedList.getItems().isEmpty());
        assertEquals("Buy milk", savedTodoItem.getTitle());
    }
}
