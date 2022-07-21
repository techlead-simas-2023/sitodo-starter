package com.example.sitodo.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class TodoListTest {

    @Test
    void testEquals() {
        TodoList first = new TodoList(List.of(new Item("Buy milk")));
        TodoList second = new TodoList(List.of(new Item("Buy milk"), new Item("Cut grass")));

        assertNotEquals(first, second);
    }

    @Test
    void testHashCode() {
        TodoList first = new TodoList(List.of(new Item("Buy milk")));
        TodoList second = new TodoList(List.of(new Item("Buy milk"), new Item("Cut grass")));

        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
