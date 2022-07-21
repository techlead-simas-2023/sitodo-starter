package com.example.sitodo.service;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@SpringBootTest
class TodoListServiceTest {

    // Challenge: We can make this unit test suite to be fully independent, i.e.
    // run without integration with Spring Boot framework. How?
    // Hint to the challenge: Erasing @SpringBootTest & @MockBean annotations
    //                        are not enough!
    //
    // If you happen to come up with a solution to the challenge, then try to
    // reflect with the discussion on software design and testing. Several
    // trigger questions:
    //
    // 1. How much responsibility does the TodoListService class currently have?
    // 2. What are the dependencies required by TodoListService class?
    //    Hint: Dependencies are not only about software packages, but also data
    //    and states required in order to initialise and execute a class' behavior.
    // 3. Why the test suite execution seemed to take longer if we run the test
    //    suite integrated with Spring Boot framework, i.e. the test class
    //    annotated with @SpringBootTest?

    @Autowired
    private TodoListService todoListService;

    @Value("${sitodo.motivation.empty}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.allFinished}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.halfFinished}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.fewItems}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @MockBean
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("Given an existing ID, getTodoListById should return an existing list")
    void getTodoListById_ok() {
        TodoList todoList = createTodoList("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        TodoList savedList = todoListService.getTodoListById(1L);

        assertFalse(savedList.getItems().isEmpty());
    }

    @Test
    @DisplayName("Suppose the list does not exist, getTodoListById should throw an exception")
    void getTodoListById_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.getTodoListById(1L));
    }

    @Test
    @DisplayName("Given a new todo item, addTodoItem should save the item into a new list")
    void addTodoItem_ok() {
        TodoItem todoItem = new TodoItem("Buy milk");
        when(todoListRepository.save(any(TodoList.class))).thenReturn(new TodoList(List.of(todoItem)));

        TodoList savedList = todoListService.addTodoItem(todoItem);
        TodoItem savedTodoItem = savedList.getItems().get(0);

        assertFalse(savedList.getItems().isEmpty());
        assertEquals("Buy milk", savedTodoItem.getTitle());
    }

    @Test
    @DisplayName("Given a todo item, addTodoItem should save the item into an existing list")
    void addTodoItem_existingList_ok() {
        TodoList list = createTodoList("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(list));

        todoListService.addTodoItem(1L, new TodoItem("Touch grass"));

        assertEquals(2, list.getItems().size(), "The numbers of items in the list: " + list.getItems().size());
    }

    @Test
    @DisplayName("Suppose the list does not exist, addTodoItem should throw an exception")
    void addTodoItem_existingList_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.addTodoItem(1L, new TodoItem("Buy milk")));
    }

    @Test
    @DisplayName("Given an existing list with an item, updateTodoItem should update the status of an item")
    void updateTodoItem_ok() {
        TodoItem item = new TodoItem(1L, "Buy milk");
        TodoList list = mock(TodoList.class);
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(list));
        when(list.getItems()).thenReturn(List.of(item));

        todoListService.updateTodoItem(1L, 1L, true);

        assertTrue(list.getItems().stream().anyMatch(TodoItem::getFinished));
    }

    @Test
    @DisplayName("Suppose the list does not exist, updateTodoItem should throw an exception")
    void updateTodoItem_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.updateTodoItem(1L, 2L, true));
    }

    @Test
    @DisplayName("Given an empty list, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_emptyList() {
        TodoList emptyList = new TodoList(Collections.emptyList());

        String message = todoListService.computeMotivationMessage(emptyList);

        assertThat(message, containsString(emptyListMessage));
    }

    @Test
    @DisplayName("Given a list with few items all unfinished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_noFinished() {
        TodoList list = createTodoList("Get outside", "Touch grass", "Breathe air", "Buy milk");

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_allFinished() {
        TodoList list = createTodoList("Get outside", "Touch grass", "Breathe air", "Buy milk");
        list.getItems().forEach(item -> item.setFinished(true));

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_halfFinished() {
        TodoList list = createTodoList("Get outside", "Touch grass", "Breathe air", "Buy milk");
        int itemCount = list.getItems().size();

        for (int i = 0; i < itemCount / 2; i++) {
            TodoItem item = list.getItems().get(i);
            item.setFinished(true);
        }

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items and single item finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_singleFinished() {
        TodoList list = createTodoList("Get outside", "Touch grass", "Breathe air", "Buy milk");
        list.getItems().get(0).setFinished(true);

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(someFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and none finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_noFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);
        TodoList list = createTodoList(items);

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_allFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);
        TodoList list = createTodoList(items);
        list.getItems().forEach(todoItem -> todoItem.setFinished(true));

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_halfFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);
        TodoList list = createTodoList(items);
        int countItems = list.getItems().size();

        for (int i = 0; i < countItems / 2; i++) {
            TodoItem item = list.getItems().get(i);
            item.setFinished(true);
        }

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and single finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_singleFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);
        TodoList list = createTodoList(items);
        list.getItems().get(0).setFinished(true);

        String message = todoListService.computeMotivationMessage(list);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(someFinishedMessage)
        ));
    }

    private TodoList createTodoList(String... items) {
        TodoList list = new TodoList(new ArrayList<>());

        Arrays.stream(items)
            .map(TodoItem::new)
            .forEach(list::addTodoItem);

        return list;
    }
}
