package com.example.sitodo.api;

import com.example.sitodo.model.Item;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoListApiController {

    private TodoListService todoListService;

    @Autowired
    public void setTodoListService(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/api/list/{id}")
    public ResponseEntity<Output> getTodoList(@PathVariable Long id) {
        TodoList found = todoListService.getTodoListById(id);

        return ResponseEntity.ok(new Output("OK", found));
    }

    @PostMapping("/api/list")
    public ResponseEntity<Output> newTodoList(String item) {
        TodoList saved = todoListService.addTodoItem(new Item(item));

        return ResponseEntity.ok(new Output("OK", saved));
    }

    record Output(String status, TodoList data) { }
}
