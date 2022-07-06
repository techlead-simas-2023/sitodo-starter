package com.example.sitodo.service;

import com.example.sitodo.model.Item;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TodoListService {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListService.class);

    private TodoListRepository todoListRepository;

    @Autowired
    public void setTodoListRepository(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public TodoList getTodoListById(Long id) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        if (result.isEmpty()) {
            final String message = String.format("TodoList(id=%d) does not exist", id);
            throw new NoSuchElementException(message);
        }

        LOG.debug("TodoList(id={}) exists", id);

        return result.get();
    }

    public TodoList addTodoItem(Item todoItem) {
        TodoList list = new TodoList(List.of(todoItem));

        return todoListRepository.save(list);
    }

    public TodoList addTodoItem(Long id, Item todoItem) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        if (result.isEmpty()) {
            final String message = String.format("TodoList(id=%d) does not exist", id);
            throw new NoSuchElementException(message);
        }

        LOG.debug("TodoList(id={}) exists", id);

        TodoList foundList = result.get();
        foundList.addItem(todoItem);

        return todoListRepository.save(foundList);
    }
}
