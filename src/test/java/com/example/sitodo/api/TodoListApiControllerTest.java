package com.example.sitodo.api;

import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.TodoListService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TodoListApiController.class)
@Tag("unit")
class TodoListApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @Test
    void getTodoList_byId_exists() throws Exception {
        when(todoListService.getTodoListById(anyLong())).thenReturn(new TodoList(List.of(new TodoItem("Buy milk"))));

        mockMvc.perform(get("/api/list/1")).andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.list.items[0].title").value("Buy milk")
        );
    }

    @Test
    void newTodoList_ok() throws Exception {
        when(todoListService.addTodoItem(any(TodoItem.class))).thenReturn(new TodoList(List.of(new TodoItem("Buy milk"))));

        mockMvc.perform(post("/api/list").content("{\"title\":\"Buy milk\"}")).andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.list.items[0].title").value("Buy milk")
        );
    }
}
