# Create a Basic Todo List

## Data Persistence Layer

Create a new class named `TodoItem` in the production code, specifically in a new package named `model`:

```java
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

## Repository Layer

Create a new Java interface named `TodoListRepository` that extends `CrudRepository` interface provided by Spring JPA.
Put the new Java interface in a new package called `repository` in the production code.
We will use it to implement a data access layer in between the data model and business logic layers.
The `TodoListRepository` implementation is short and can be seen in the following code snippet:

```java
// Removed some import statements for brevity.
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends CrudRepository<TodoItem, Long> {
}
```

## Service Layer

Create a new package called `service` in the production code.
Then, create a new class named `TodoListService` in the new package.
We will use `service` package to contain classes related to service layer of the app,
i.e. classes that provide implementations related to business logic.
The content of `TodoListService` is as follows:

```java
// Removed some import statements for brevity.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoListService {

    private TodoListRepository todoListRepository;

    @Autowired
    public void setTodoListRepository(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }
}
```

Since we will implement a basic todo list that shows a list of todo items,
we need to implement methods that can add and retrieve todo items:

```java
public class TodoListService {
    // Omitted for brevity
    private TodoListRepository todoListRepository;

    public List<TodoItem> getTodoItems() {
        return todoListRepository.findAll();
    }

    public TodoItem addTodoItem(TodoItem todoItem) {
        return todoListRepository.save(todoItem);
    }
}
```

## Test Initial Implementation

You might question: "Do we need to test the repository and service classes?"
The implementation of both layers is quite simple at this point, so one might wonder if it is worth testing them.
The repository layer can be left untested at the moment because we did not customise the inherited implementation.
The service layer, however, needs to be tested because it is related to business logic that is domain-specific to the problem.

Create a new class named `TodoListService` in the test code and ensure the package structure follows the production code.
In the new class, implement the test cases that verify the expected correct behaviour of both `getTodoItems` and `addTodoItem`:

```java
import com.example.sitodo.model.TodoItem;
import com.example.sitodo.repository.TodoListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoListServiceTest {

    @Autowired
    private TodoListService todoListService;

    @MockBean
    private TodoListRepository todoListRepository;

    @Test
    void getTodoItems_someItems_ok() {
        when(todoListRepository.findAll()).thenReturn(List.of(
            new TodoItem("A"),
            new TodoItem("B"),
            new TodoItem("C")
        ));
        
        List<TodoItem> todoItems = todoListService.findAll();
        
        assertEquals(3, todoItems.size());
    }

    @Test
    void addTodoItem_ok() {
        TodoItem todoItem = new TodoItem("Buy milk");
        when(todoListRepository.save(any(TodoItem.class))).thenReturn(todoItem);

        TodoItem savedTodoItem = todoListService.addTodoItem(todoItem);

        assertEquals("Buy milk", savedTodoItem.getTitle());
    }
}
```

Maybe you noticed that the tests look different from the one you created in the previous section.
The tests above implement a mock object that serves as a stand-in of an actual object during testing.
The mock object can be configured to simulate a behaviour specific during testing.
We use a mock object to ensure the test in the service layer is independent of the repository layer.

## Back to Controller and View

You have implemented the business logic of adding and listing a todo list.
The next step is to implement the controller to handle requests from the user.
But before that, in spirit of TDD, let us create a test first.
Let us start by implementing a method to retrieve the todo list.

Create a new class named `TodoListControllerTest` in the test code:

> You know where to put it, right?
> Controller classes go into controller package.
> Maintain proper separation of concern in your codebase!

```java
// Removed some import statements for brevity.
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
@Tag("unit")
class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoListService todoListService;
    
    // New annotation!
    // @DisplayName allows you to customise the name of a test case.
    @Test
    @DisplayName("HTTP GET '/list' retrieves list view")
    void showList_correctView() throws Exception {
        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            view().name("list")
        );
    }

    @Test
    @DisplayName("HTTP GET '/list' returns an HTML page")
    void showList_returnHtml() throws Exception {
        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("</html>"))
        );
    }

    @Test
    @DisplayName("HTTP GET '/list' returns an HTML page with non-empty list")
    void showList_withSampleData_ok() throws Exception {
        TodoItem mockTodoItem = new TodoItem("Buy milk");
        when(todoListService.getTodoItems()).thenReturn(List.of(mockTodoItem));

        mockMvc.perform(get("/list")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            content().string(containsString("Buy milk"))
        );
    }
}
```

Implement the actual controller in the production code:

```java
// Removed import statements for brevity
@Controller
public class TodoListController {

    private TodoListService todoListService;

    @Autowired
    public void setTodoListService(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/list")
    public String showList(Model model) {
        List<TodoItem> todoItems = todoListService.getTodoItems();
        
        model.add("todoList", todoItems);
        
        return "list";
    }
}
```

Do not forget to create the HTML view of the todo list.
Create a new HTML view called `list.html` in `templates` directory at `src/main/resources`:

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
        <span class="fs-4">SITODO</span>
    </header>
    <table id="id_list_table" class="table caption-top">
        <caption class="display-1">Your Todo List</caption>
        <thead class="table-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Title</th>
            <th scope="col">Status</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="item : ${todoList}">
            <td th:text="${item.id}">1</td>
            <td th:text="${item.title}">Cook rice</td>
            <td th:text="${item.finished} ? 'Finished' : 'Not Finished'">Not Finished</td>
        </tr>
        </tbody>
    </table>
    <form action="/list" method="post">
        <input type="text" id="id_new_item" name="item_text" placeholder="Enter an item"/>
        <button type="submit" class="btn btn-primary btn-sm">Enter</button>
    </form>
</div>
<script th:src="@{/webjars/bootstrap/5.1.3/dist/js/bootstrap.bundle.js}"></script>
</body>
</html>
```

The HTML view example above requires the [Bootstrap CSS framework](https://getbootstrap.com/).
You can either include Bootstrap from a content delivery network (CDN),
or add a new dependency in `pom.xml` that adds Bootstrap into the project.
If you choose to update the project dependencies in `pom.xml`,
use the following Maven dependency that includes Bootstrap:

```xml
<dependency>
    <groupId>org.webjars.npm</groupId>
    <artifactId>bootstrap</artifactId>
    <version>5.1.3</version>
</dependency>
```

## Complete the Basic Functionality

You have known the overall groundwork required to implement a feature in the TDD approach.
The next task is to ensure the user can actually create a new todo item.
Now try to work on your own.
But do not worry, there will be hints on how to complete it.

Your tasks are as follows:

1. Add a new test case in `TodoListControllerTest` that verifies the correctness of incoming HTTP POST message.
   Try to figure out how to use `MockMvc` to simulate an HTTP POST message with a data payload
   (i.e. the new todo item submitted via an HTML form).
2. Add a new method in `TodoListController` that handles incoming HTTP POST message containing the new todo item.
   Use `@PostMapping` annotation to designate the new method as the handler of an incoming HTTP POST message to `/list` path.
   You also need to add a parameter into the method, so you can obtain the new todo item in the HTTP POST message.

Try to run the application from time to time.
While having test suites that are executed by a test runner is important,
do not forget to run the app manually and try using it as an actual user.
You can run the app by executing `main()` method in `SitodoApplication` class,
or by calling `mvnw spring-boot:run` Maven command.

If you have finished, do not forget to save your latest work as a commit and push it to GitHub:

```shell
git add <path to the changed files>
git commit
git push
```
