package com.example.sitodo.controller;

import com.example.sitodo.dto.TodoItemDto;
import com.example.sitodo.dto.TodoListDto;
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.service.MotivationMessageService;
import com.example.sitodo.service.TodoListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoListController.class)
@Tag("unit")
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private MotivationMessageService motivationMessageService;

    @Test
    @DisplayName("HTTP GET '/list' retrieves list view")
    void showList_resolvesToIndex() throws Exception {
        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            view().name("list")
        );
    }

    @Test
    @DisplayName("HTTP GET '/list' returns an HTML page")
    void showList_returnsHtml() throws Exception {
        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("</html>"))
        );
    }

    @Test
    @DisplayName("HTTP GET '/list/{id}' returns an HTML page with non-empty list")
    void showList_byId_returnsHtml() throws Exception {
        when(todoListService.getTodoListById(1L)).thenReturn(new TodoListDto(1L, List.of(new TodoItemDto(1L, "Buy milk", false))));

        mockMvc.perform(get("/list/1")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("<table")),
            content().string(containsString("<tr")),
            content().string(containsString("Buy milk")),
            content().string(containsString("</html>"))
        );
    }

    @Test
    @DisplayName("Suppose the given ID does not exist, HTTP GET '/list/{id}' returns an error page")
    void showList_byId_notFound() throws Exception {
        when(todoListService.getTodoListById(anyLong())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/list/1")).andExpectAll(
            content().string(containsString("Not Found"))
        );
    }

    @Test
    @DisplayName("HTTP GET '/list/{id}/update/{item_id}' successfully updated status of an item")
    void updateItem_ok() throws Exception {
        TodoListDto todoListSingleItem = new TodoListDto(1L, List.of(new TodoItemDto(1L, "Buy milk", true)));

        when(todoListService.setTodoItemFinished(1L, 1L, true))
            .thenReturn(todoListSingleItem);
        when(todoListService.getTodoListById(1L)).thenReturn(todoListSingleItem);

        mockMvc.perform(get("/list/1/update/1?finished=true")).andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/list/1")
        );

        mockMvc.perform(get("/list/1")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("<table")),
            content().string(containsString("<tr")),
            content().string(containsString("Buy milk")),
            content().string(containsString("Finished")),
            content().string(containsString("</html>"))
        );

        // Note: Notice that we don't actually verify whether the item was successfully
        // updated. It is all pre-scripted in the mock object. We dictate how the SUT
        // (Software Under Test) should response when given a stimulus during execution
        // of a test case. In this example, we told the service layer to provide a TodoList
        // object that supposedly has been modified to the controller. The controller
        // will use the mock object from service layer as input for the view layer.
        // The view layer then use the mock object as data model for rendering the HTML.
    }

    @Test
    @DisplayName("HTTP POST /list successfully added a new item")
    void newItem_ok() throws Exception {
        when(todoListService.addTodoItem(any())).thenReturn(new TodoListDto(1L, Collections.emptyList()));

        mockMvc.perform(
            post("/list")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Touch grass")
        ).andExpectAll(status().is3xxRedirection());
    }

    @Test
    @DisplayName("HTTP POST /list rejected invalid item")
    void newItem_invalid() throws Exception {
        mockMvc.perform(
            post("/list")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "")
        ).andExpectAll(status().isOk());
    }

    @Test
    @DisplayName("HTTP POST /list/{id} successfully added a new item into a list")
    void newItem_withId_ok() throws Exception {
        when(todoListService.addTodoItem(anyLong(), any())).thenReturn(new TodoListDto(1L, Collections.emptyList()));

        mockMvc.perform(
            post("/list/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Buy milk")
        ).andExpectAll(status().is3xxRedirection());
    }

    @Test
    @DisplayName("HTTP POST /list/{id} rejected invalid item")
    void newItem_withId_invalid() throws Exception {
        when(todoListService.getTodoListById(1L)).thenReturn(new TodoListDto(1L, Collections.emptyList()));

        mockMvc.perform(
            post("/list/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "")
        ).andExpectAll(status().isOk());
    }

    private TodoList createMockTodoList(Long id, TodoItem... items) {
        TodoList mockTodoList = mock(TodoList.class);

        when(mockTodoList.getId()).thenReturn(id);
        when(mockTodoList.getItems()).thenReturn(List.of(items));

        return mockTodoList;
    }

    private TodoItem createMockTodoItem(Long id, String title) {
        TodoItem mockTodoItem = mock(TodoItem.class);

        when(mockTodoItem.getId()).thenReturn(id);
        when(mockTodoItem.getTitle()).thenReturn(title);
        when(mockTodoItem.getFinished()).thenCallRealMethod();

        return mockTodoItem;
    }
}
