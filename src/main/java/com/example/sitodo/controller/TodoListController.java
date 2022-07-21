package com.example.sitodo.controller;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.TodoListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
public class TodoListController {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListController.class);

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

        model.addAttribute("todoList", foundTodoList);
        model.addAttribute("motivationMessage", todoListService.computeMotivationMessage(foundTodoList));

        return "list";
    }

    @PostMapping("/list")
    public String newItem(@RequestParam("item_text") String item) {
        LOG.debug("New item: {}", item);

        TodoList saved = todoListService.addTodoItem(new TodoItem(item));

        LOG.debug("New item ID: {}", saved.getId());

        return redirectToList(saved.getId());
    }

    @PostMapping("/list/{id}")
    public String newItem(@PathVariable("id") Long id,
                          @RequestParam("item_text") String item) {
        LOG.debug("New item: {}", item);

        TodoList saved = todoListService.addTodoItem(id, new TodoItem(item));

        LOG.debug("New item ID: {}", saved.getId());

        return redirectToList(saved.getId());
    }

    @GetMapping("/list/{list_id}/update/{item_id}")
    public String updateItem(@PathVariable("list_id") Long listId,
                             @PathVariable("item_id") Long itemId,
                             @RequestParam("finished") Boolean finished) {
        TodoList updated = todoListService.updateTodoItem(listId, itemId, finished);

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
