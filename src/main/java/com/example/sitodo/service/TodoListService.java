package com.example.sitodo.service;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TodoListService {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListService.class);
    private static final String TODO_LIST_DOES_NOT_EXIST_FMT = "TodoList(id=%d) does not exist";

    private TodoListRepository todoListRepository;

    @Value("${sitodo.motivation.empty}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.halfFinished}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.allFinished}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.fewItems}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.fewItemsThreshold:5}")
    private int fewItemsThreshold;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Autowired
    public void setTodoListRepository(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public TodoList getTodoListById(Long id) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        if (result.isEmpty()) {
            final String message = String.format(TODO_LIST_DOES_NOT_EXIST_FMT, id);
            throw new NoSuchElementException(message);
        }

        return result.get();
    }

    public TodoList addTodoItem(TodoItem todoItem) {
        TodoList list = new TodoList(List.of(todoItem));

        return todoListRepository.save(list);
    }

    public TodoList addTodoItem(Long id, TodoItem todoItem) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        if (result.isEmpty()) {
            final String message = String.format(TODO_LIST_DOES_NOT_EXIST_FMT, id);
            throw new NoSuchElementException(message);
        }

        TodoList foundList = result.get();
        foundList.addTodoItem(todoItem);

        return todoListRepository.save(foundList);
    }

    public TodoList updateTodoItem(Long listId, Long itemId, Boolean status) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(listId);

        if (result.isEmpty()) {
            final String message = String.format(TODO_LIST_DOES_NOT_EXIST_FMT, listId);
            throw new NoSuchElementException(message);
        }

        TodoList list = result.get();

        for (TodoItem item : list.getItems()) {
            if (item.getId().equals(itemId)) {
                item.setFinished(status);
            }
        }

        return todoListRepository.save(list);
    }

    public String computeMotivationMessage(TodoList todoList) {
        List<TodoItem> items = todoList.getItems();
        final long totalItems = (items != null) ? items.size() : 0;
        final long totalFinishedItems = (items != null) ?
            items.stream().filter(TodoItem::getFinished).count() : 0;

        LOG.debug("Total Items: {}; Total Finished Items: {}", totalItems, totalFinishedItems);

        String output = "";

        if (totalItems == 0) {
            output += emptyListMessage;
        } else if (totalItems < manyItemsThreshold) {
            output += fewItemsMessage;

            if (totalFinishedItems == totalItems) {
                output += " " + allFinishedMessage;
            } else if (totalFinishedItems == 0) {
                output += " " + noFinishedMessage;
            } else if (totalFinishedItems < totalItems && totalFinishedItems >= totalItems / 2) {
                output += " " + halfFinishedMessage;
            } else {
                output += someFinishedMessage;
            }
        } else {
            output += manyItemsMessage;

            if (totalFinishedItems == totalItems) {
                output += " " + allFinishedMessage;
            } else if (totalFinishedItems == 0) {
                output += " " + noFinishedMessage;
            } else if (totalFinishedItems < totalItems && totalFinishedItems >= totalItems / 2) {
                output += " " + halfFinishedMessage;
            } else {
                output += someFinishedMessage;
            }
        }

        LOG.debug("Resulting output: {}", output);

        return output;
    }
}
