package com.example.sitodo.service.impl;

import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.form.TodoItemForm;
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import com.example.sitodo.service.MotivationMessageService;
import com.example.sitodo.service.TodoListService;
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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Tag("unit")
@SpringBootTest
class TodoListServiceImplTest {

    // Challenge: We can make this unit test suite to be fully independent, i.e.
    // run without integration with the Spring Boot framework. How?
    // Hint to the challenge: Erasing @SpringBootTest & @MockBean annotations
    //                        are not enough!
    //
    // Since the current version requires @SpringBootTest annotation, the tests
    // need a PostgreSQl instance running during test execution, albeit there
    // will not be any new rows inserted into the database.
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

    @Autowired
    private MotivationMessageService motivationMessageService;

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
        TodoList todoList = createTodoListEntity("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        TodoListDto savedList = todoListService.getTodoListById(1L);

        assertFalse(savedList.items().isEmpty());
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
        TodoItem fakeTodoItem = new TodoItem(1L, "Buy milk");
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.setItems(List.of(fakeTodoItem));
        when(todoListRepository.save(any(TodoList.class)))
            .thenReturn(fakeTodoList);

        TodoListDto newTodoList = todoListService.addTodoItem(createTodoItemForm("Buy milk"));

        assertFalse(newTodoList.items().isEmpty());
    }

    @Test
    @DisplayName("Given a todo item, addTodoItem should save the item into an existing list")
    void addTodoItem_existingList_ok() {
        TodoItem fakeTodoItem = new TodoItem(1L, "Buy milk");
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.addTodoItem(fakeTodoItem);
        when(todoListRepository.findById(anyLong()))
            .thenReturn(Optional.of(fakeTodoList));
        when(todoListRepository.save(fakeTodoList))
            .thenReturn(fakeTodoList);

        TodoListDto updatedTodoList = todoListService.addTodoItem(1L, createTodoItemForm("Touch grass"));

        assertEquals(2L, updatedTodoList.countTotal());
    }

    @Test
    @DisplayName("Suppose the list does not exist, addTodoItem should throw an exception")
    void addTodoItem_existingList_exception() {
        assertThrows(NoSuchElementException.class, () -> todoListService.addTodoItem(1L, createTodoItemForm("Buy milk")));
    }

    @Test
    @DisplayName("Given an existing list with an item, setTodoItemFinished should update the status of an item")
    void updateTodoItem_ok() {
        TodoList fakeTodoList = new TodoList();
        fakeTodoList.setId(1L);
        fakeTodoList.addTodoItem(new TodoItem(1L, "Buy milk"));
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(fakeTodoList));
        when(todoListRepository.save(fakeTodoList)).thenReturn(fakeTodoList);

        TodoListDto updatedTodoList = todoListService.setTodoItemFinished(1L, 1L, true);

        assertEquals(1, updatedTodoList.countFinishedItems());
    }

    @Test
    @DisplayName("Suppose the list does not exist, setTodoItemFinished should throw an exception")
    void updateTodoItem_exception() {
        assertThrows(NoSuchElementException.class, () -> todoListService.setTodoItemFinished(1L, 2L, true));
    }

    @Test
    @DisplayName("Given an empty list, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_emptyList() {
        String message = motivationMessageService.computeMotivationMessage(0, 0);

        assertThat(message, containsString(emptyListMessage));
    }

    @Test
    @DisplayName("Given a list with few items all unfinished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_noFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 0);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_allFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 4);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_halfFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 2);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items and single item finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_singleFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 1);

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

        String message = motivationMessageService.computeMotivationMessage(items.length, 0);

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

        String message = motivationMessageService.computeMotivationMessage(items.length, items.length);

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

        String message = motivationMessageService.computeMotivationMessage(items.length, items.length / 2);

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

        String message = motivationMessageService.computeMotivationMessage(items.length, 1);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(someFinishedMessage)
        ));
    }

    private TodoList createTodoListEntity(String... items) {
        TodoList list = new TodoList(new ArrayList<>());

        Arrays.stream(items)
            .map(TodoItem::new)
            .forEach(list::addTodoItem);

        return list;
    }

    private static TodoItemForm createTodoItemForm(String title) {
        TodoItemForm newTodoItem = new TodoItemForm();
        newTodoItem.setTitle(title);

        return newTodoItem;
    }
}
