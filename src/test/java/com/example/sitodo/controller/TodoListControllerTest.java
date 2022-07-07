package com.example.sitodo.controller;

import com.example.sitodo.model.Item;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.TodoListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TodoListController.class)
@Tag("unit")
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @Test
    @DisplayName("HTTP GET '/list' retrieves list view")
    void showList_resolvesToIndex() throws Exception {
        mockMvc.perform(get("/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(content().encoding(UTF_8))
            .andExpect(view().name("list"));
    }

    @Test
    @DisplayName("HTTP GET '/list' returns an HTML page")
    void showList_returnsHtml() throws Exception {
        mockMvc.perform(get("/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(content().encoding(UTF_8))
            .andExpect(content().string(containsString("</html>")));
    }

    @Test
    @DisplayName("HTTP GET '/list/{id}' returns an HTML page with non-empty list")
    void showList_byId_returnsHtml() throws Exception {
        when(todoListService.getTodoListById(anyLong())).thenReturn(new TodoList(List.of(new Item("Buy milk"))));

        mockMvc.perform(get("/list/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
            .andExpect(content().encoding(UTF_8))
            .andExpect(content().string(containsString("<table")))
            .andExpect(content().string(containsString("<tr")))
            .andExpect(content().string(containsString("Buy milk")))
            .andExpect(content().string(containsString("</html>")));
    }
}
