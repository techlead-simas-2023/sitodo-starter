package com.example.sitodo.controller;

import com.example.sitodo.model.TodoItem;
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
        TodoItem mockTodoItem = createMockTodoItem(1L, "Buy milk");
        TodoList mockList = mock(TodoList.class);
        when(mockList.getId()).thenReturn(1L);
        when(mockList.getItems()).thenReturn(List.of(mockTodoItem));
        when(todoListService.getTodoListById(anyLong())).thenReturn(mockList);

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
    @DisplayName("HTTP GET '/list/{id}/update/{item_id}' successfully updated status of an item")
    void updateItem_ok() throws Exception {
        TodoItem mockTodoItem = createMockTodoItem(1L, "Buy milk");
        TodoList mockList = createMockTodoList(1L, mockTodoItem);
        mockTodoItem.setFinished(true);

        when(todoListService.updateTodoItem(anyLong(), anyLong(), anyBoolean())).thenReturn(mockList);

        mockMvc.perform(get("/list/1/update/1?finished=true")).andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/list/1")
        );

        when(todoListService.getTodoListById(anyLong())).thenReturn(mockList);

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

    private TodoList createMockTodoList(Long id, TodoItem ... items) {
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
