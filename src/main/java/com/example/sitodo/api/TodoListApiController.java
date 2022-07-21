package com.example.sitodo.api;

import com.example.sitodo.model.TodoItem;
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
        TodoList saved = todoListService.addTodoItem(new TodoItem(item));

        return ResponseEntity.ok(new Output("OK", saved));
    }

    private class Output {

        private final String status;
        private final TodoList list;

        public Output(String status, TodoList list) {
            this.status = status;
            this.list = list;
        }

        public String getStatus() { return status; }
        public TodoList getList() { return list; }
    }
    /*
        In Java version 17 onwards, the inner class above can be simplified into
        a **record type**. For example: record Output(String status, TodoList list) { }

        Or if you prefer using Project Lombok annotation library, you can mimic
        similar Java 17's record type by using @Data annotation:

        @Data
        private class Output {

            private final String status;
            private final TodoList list;
        }
    */
}
