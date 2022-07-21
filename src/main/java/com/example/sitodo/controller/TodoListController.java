package com.example.sitodo.controller;

import com.example.sitodo.model.Item;
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
    public String showList() {
        return "list";
    }

    @GetMapping("/list/{id}")
    public String showList(@PathVariable("id") Long id, Model model) {
        try {
            TodoList found = todoListService.getTodoListById(id);

            model.addAttribute("items", found.getItems());
            model.addAttribute("id_list", id);

            return "list";
        } catch (NoSuchElementException exception) {
            // TODO Implement error handling
            LOG.warn(exception.getMessage());

            return "404";
        }
    }

    @PostMapping("/list")
    public String newItem(@RequestParam("item_text") String item) {
        LOG.debug("New item: {}", item);

        TodoList saved = todoListService.addTodoItem(new Item(item));

        return "redirect:/list/" + saved.getId();
    }

    @PostMapping("/list/{id}")
    public String newItem(@PathVariable(value = "id") Long id, @RequestParam("item_text") String item) {
        LOG.debug("New item: {}", item);

        TodoList saved = todoListService.addTodoItem(id, new Item(item));

        return "redirect:/list/" + saved.getId();
    }
}
