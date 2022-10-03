# Create a One-To-Many List

Today you will update the application by making the user able to create multiple lists each time they open the app.
At the same time, you will practice a Git workflow called **Feature Branch** workflow.

First, make sure your local Git repository has the same history as the remote Git repository on GitHub.
If you have any Git commits that have not been pushed to GitHub, please commit them now.
Similarly, if you made changes on GitHub, ensure the Git commit history is pulled into your local Git repository.

```shell
# Push commits
git push origin main
# Pull commits from remote Git repository into the currently active branch
git pull origin main
```

According to Feature Branch workflow, any new changes to the codebase should be done in a separate branch from the main branch.
Once the changes have been tested, the branch is merged into the main branch.
This is to ensure any ongoing development will not break the application that built from the main branch.

## Data Persistence Layer

Let us start from the data persistence layer.
Open a shell (or terminal in the IDE) and create a new branch named `todo-list-v2` using `git branch` and `git checkout` commands:

```shell
git branch todo-list-v2
git checkout todo-list-v2
# Or shorter form:
git checkout -b todo-list-v2
```

Open `model` package in the production code and update `TodoItem` class with following code snippets:

```java
// TodoItem.java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private Boolean finished = Boolean.FALSE;

    public TodoItem(String title) {
        this.title = title;
    }

    public TodoItem(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
```

Then, add a new model class named `TodoList` in the same `model` package as `TodoItem` class:

```java
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TodoItem> items;

    public TodoList(List<TodoItem> items) {
        this.items= items;
    }

    public void addTodoItem(TodoItem item) {
        items.add(item);
    }
}
```

Based on the new data model, all instances of `TodoItem` will be associated to a `TodoList`.
Or in other words, a `TodoList` will have one-to-many relationship to `TodoItem`.
Certainly, this will require you to update the code across every layer.

## Repository Layer

Open `repository` package in the production code and update `TodoListRepository` class with following code snippets:

```java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends CrudRepository<TodoList, Long> {
}
```

Based on the change above, `TodoItem` was replaced with `TodoList`.
All queries for querying todo items will use the one-to-many relationship mapping possessed by `TodoList`.

## Service Layer

Open `service` package in the production code and update `TodoListService` class with following code snippets:

```java
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.model.TodoList;
import com.example.sitodo.repository.TodoListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TodoListService {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListService.class);
    private static final String TODO_LIST_DOES_NOT_EXIST_FMT = "TodoList(id=%d) does not exist";

    private TodoListRepository todoListRepository;

    @Autowired
    public void setTodoListRepository(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public TodoList getTodoListById(Long id) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        return result.get();
    }

    public TodoList addTodoItem(TodoItem todoItem) {
        TodoList list = new TodoList(List.of(todoItem));

        return todoListRepository.save(list);
    }

    public TodoList addTodoItem(Long id, TodoItem todoItem) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(id);

        TodoList foundList = result.get();
        foundList.addTodoItem(todoItem);

        return todoListRepository.save(foundList);
    }

    public TodoList updateTodoItem(Long listId, Long itemId, Boolean status) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(listId);

        // TODO: Implement me!
        return null;
    }
    
    public Boolean deleteTodoItem(Long listId, Long itemId) throws NoSuchElementException {
        Optional<TodoList> result = todoListRepository.findById(listId);

        // TODO: Implement me!
        return Boolean.FALSE;
    }
}
```

Update the test cases for `TodoListService` in the test code:

```java
// TodoListServiceTest.java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoListServiceTest {

    @Autowired
    private TodoListService todoListService;

    @MockBean
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("Given an existing ID, getTodoListById should return an existing list")
    void getTodoListById_ok() {
        TodoList todoList = createTodoList("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(todoList));

        TodoList savedList = todoListService.getTodoListById(1L);

        assertFalse(savedList.getItems().isEmpty());
    }

    @Test
    @DisplayName("Suppose the list does not exist, getTodoListById should throw an exception")
    void getTodoListById_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.getTodoListById(1L));
    }

    @Test
    @DisplayName("Given a new todo item, addTodoItem should save the item into a new list")
    void addTodoItem_ok() {
        TodoItem todoItem = new TodoItem("Buy milk");
        when(todoListRepository.save(any(TodoList.class))).thenReturn(new TodoList(List.of(todoItem)));

        TodoList savedList = todoListService.addTodoItem(todoItem);
        TodoItem savedTodoItem = savedList.getItems().get(0);

        assertFalse(savedList.getItems().isEmpty());
        assertEquals("Buy milk", savedTodoItem.getTitle());
    }

    @Test
    @DisplayName("Given a todo item, addTodoItem should save the item into an existing list")
    void addTodoItem_existingList_ok() {
        TodoList list = createTodoList("Buy milk");
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(list));

        todoListService.addTodoItem(1L, new TodoItem("Touch grass"));

        assertEquals(2, list.getItems().size(), "The numbers of items in the list: " + list.getItems().size());
    }

    @Test
    @DisplayName("Suppose the list does not exist, addTodoItem should throw an exception")
    void addTodoItem_existingList_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.addTodoItem(1L, new TodoItem("Buy milk")));
    }

    @Test
    @DisplayName("Given an existing list with an item, updateTodoItem should update the status of an item")
    void updateTodoItem_ok() {
        // TODO: Implement me!
    }

    @Test
    @DisplayName("Suppose the list does not exist, updateTodoItem should throw an exception")
    void updateTodoItem_exception() {
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> todoListService.updateTodoItem(1L, 2L, true));
    }
    
    // TODO: Create tests for deleteTodoItem

    private TodoList createTodoList(String... items) {
        TodoList list = new TodoList(new ArrayList<>());

        Arrays.stream(items)
            .map(TodoItem::new)
            .forEach(list::addTodoItem);

        return list;
    }
}
```

Notice that there are several methods and TODO comments that need to be resolved.

## Controller

Open `controller` package in the production code and update `TodoListController` class with following code snippets:

```java
// TodoListController.java
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

        return "list";
    }

    @GetMapping("/list/{id}")
    public String showList(@PathVariable("id") Long id, Model model) {
        TodoList foundTodoList = todoListService.getTodoListById(id);

        model.addAttribute("todoList", foundTodoList);

        return "list";
    }

    @PostMapping("/list")
    public String newItem(@RequestParam("item_text") String item) {
        TodoList saved = todoListService.addTodoItem(new TodoItem(item));

        return redirectToList(saved.getId());
    }

    @PostMapping("/list/{id}")
    public String newItem(@PathVariable("id") Long id,
                          @RequestParam("item_text") String item) {
        TodoList saved = todoListService.addTodoItem(id, new TodoItem(item));

        return redirectToList(saved.getId());
    }

    @GetMapping("/list/{list_id}/update/{item_id}")
    public String updateItem(@PathVariable("list_id") Long listId,
                             @PathVariable("item_id") Long itemId,
                             @RequestParam("finished") Boolean finished) {
        return ""; // TODO: Implement me!
    }
    
    // TODO: Create a method named deleteItem() that will remove a todo item from a todo list.
    //       The arguments can be similar to the updateItem() above.
    
    @ExceptionHandler
    public String handleException(NoSuchElementException exception) {
        return "404";
    }

    private String redirectToList(Long id) {
        return String.format("redirect:/list/%d", id);
    }
}
```

Update the test cases for `TodoListController` in the test code:

```java
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
import java.util.NoSuchElementException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoListController.class)
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
        // TODO: Implement me!
    }
    
    // TODO: Create the tests for ensuring the correctness of deleteItem() method from the controller.

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
```

Similar to the previous layer, there are some TODO comments and methods need to be implemented.

## View

Update `list.html` view in the `templates` directory with the updated HTML:

```html
<!doctype html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link th:rel="stylesheet" th:href="@{/webjars/bootstrap/5.1.3/dist/css/bootstrap.css}"/>

    <title>Sitodo</title>
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
        <a th:href="@{${todoList.id != null} ? '/list/' + ${todoList.id} : '/list'}"
           class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
            <img class="bi me-2" src="/images/logo_placeholder.png" width="160" height="40" alt="logo"/>
            <span class="fs-4">SITODO</span>
        </a>
    </header>
    <table id="id_list_table" class="table caption-top">
        <caption class="display-1">Your Todo List</caption>
        <thead class="table-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Title</th>
            <th scope="col">Status</th>
            <th scope="col">Update</th>
            <th scope="col">Delete</th>
        </tr>
        </thead>
        <tbody>
        <tr th:if="${todoList.id != null}" th:each="item : ${todoList.items}">
            <td th:text="${item.id}">1</td>
            <td th:text="${item.title}">Cook rice</td>
            <td th:text="${item.finished} ? 'Finished' : 'Not Finished'">Not Finished</td>
            <td th:if="${item.finished}">
                <a class="sitodo-finish-link" th:href="@{/list/{list_id}/update/{item_id}(list_id=${todoList.id},item_id=${item.id},finished='false')}">
                    <span>Mark as 'Not Finished'</span>
                </a>
            </td>
            <td th:unless="${item.finished}">
                <a class="sitodo-finish-link" th:href="@{/list/{list_id}/update/{item_id}(list_id=${todoList.id},item_id=${item.id},finished='true')}">
                    <span>Mark as 'Finished'</span>
                </a>
            </td>
            <td>
                <a class="sitodo-finish-link"> <!-- TODO: Implement a link that will trigger the delete todo item operation on the controller -->
                    <span>Delete</span>
                </a>
            </td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="4">
                <p id="motivation_message" th:text="${motivationMessage}">A motivation message</p>
            </td>
        </tr>
        </tfoot>
    </table>
    <form th:action="@{${todoList.id != null} ? '/list/' + ${todoList.id} : '/list'}" method="post">
        <input type="text" id="id_new_item" name="item_text" placeholder="Enter an item"/>
        <button type="submit" class="btn btn-primary btn-sm">Enter</button>
    </form>
</div>
<script th:src="@{/webjars/bootstrap/5.1.3/dist/js/bootstrap.bundle.js}"></script>
</body>
</html>
```

## The Big Assignment

You have seen that there are several TODO comments and methods that are empty or need to be resolved.
Your task in this section is to resolve all TODO comments and implement methods that are still empty.
Since you are working in a feature branch, any commits you pushed will not break the app that is currently running on Heroku.

At the end of the task, make a Pull Request that will merge `todo-list-v2` branch into the main branch.
Make sure `ci` workflow still passes before merging the branch!

After you merged `todo-list-v2` into the main branch,
switch back to the main branch in your local Git repository
and pull the latest commit from GitHub:

```shell
git checkout main
git pull
# Optional: delete local branch
git branch -D todo-list-v2
```
