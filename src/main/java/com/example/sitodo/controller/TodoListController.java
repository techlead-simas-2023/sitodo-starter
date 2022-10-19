package com.example.sitodo.controller;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.TodoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@Slf4j
public class TodoListController {

    private TodoListService todoListService;

    @Autowired
    public void setTodoListService(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/list")
    public String showList(TodoList todoList, Model model) {
        model.addAttribute("todoList", todoList);
        model.addAttribute("motivationMessage", todoListService.computeMotivationMessage(todoList));

        return "list";
    }

    @GetMapping("/list/{id}")
    public String showList(@PathVariable("id") Long id, Model model) {
        TodoList foundTodoList = todoListService.getTodoListById(id);
        log.debug("Show list with ID {}", foundTodoList.getId());

        model.addAttribute("todoList", foundTodoList);
        model.addAttribute("motivationMessage", todoListService.computeMotivationMessage(foundTodoList));

        return "list";
    }

    @PostMapping("/list")
    public String newItem(@RequestParam("item_text") String item) {
        TodoList saved = todoListService.addTodoItem(new TodoItem(item));
        log.debug("Saved a new item into a new list with ID {}", saved.getId());

        return redirectToList(saved.getId());
    }

    @PostMapping("/list/{id}")
    public String newItem(@PathVariable("id") Long id,
                          @RequestParam("item_text") String item) {
        TodoList saved = todoListService.addTodoItem(id, new TodoItem(item));
        log.debug("Saved a new item into a list with ID {}", saved.getId());

        return redirectToList(saved.getId());
    }

    @GetMapping("/list/{list_id}/update/{item_id}")
    public String updateItem(@PathVariable("list_id") Long listId,
                             @PathVariable("item_id") Long itemId,
                             @RequestParam("finished") Boolean finished) {
        TodoList updated = todoListService.updateTodoItem(listId, itemId, finished);
        log.debug("Updated an item in a list with ID {}", updated.getId());

        return redirectToList(updated.getId());
    }

    @ExceptionHandler
    public String handleException(NoSuchElementException exception) {
        return "404";
    }

    private String redirectToList(Long id) {
        return String.format("redirect:/list/%d", id);
    }
}
