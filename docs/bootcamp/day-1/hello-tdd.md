# Hello, Test-Driven Development

Let us start by creating a controller class named `HelloController` in `src/main/java` directory,
specifically under the package that was initialised by Spring Initialzr:

```java
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String showHello() {
        return "";
    }
}
```

We left the implementation empty for now.
This is called as stubbing or creating an empty implementation. 
If the return value of a method in a controller class is a string,
then the framework expects the return value contains the name of a Web page template.

Notice there are two directories in `src` directory: `main` and `test`.
The `main` directory contains the actual code for implementing the application.
We often call the actual code as the **production code**.
In contrast, the `test` directory contains the code for testing the application.
It is also known as the **test code**.

## My First Test Case

Create a new class named `HelloControllerTest` in `controller` package of the test code.
The class will contain one or more instance methods that will execute the test cases.
Let us start with a test case that verifies `showHello` method uses a Web page template named `hello`.
Create a method named `showHello_ok` in `HelloControllerTest`:

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest {
    
    @Test
    void showHello_ok() {
        // [Setup]
        HelloController helloController = new HelloController();
        
        // [Exercise]
        String result = helloController.showHello();
        
        // [Verify]
        assertEquals("hello", result);
        
        // [Teardown]
        // Do nothing
    }
}
```

The method that implements a test case can be structured into four sections:
**Setup**, **Exercise**, **Verify**, and **Teardown** sections.
**Setup** is used to initialise the test environment (e.g. the object to be tested, sample data, load test case input).
**Exercise**, as its name implies, executes the procedures that invoke the production code that are being tested.
**Verify** contains procedures that verify the result from the **Exercise**.
**Teardown** usually contains procedures to clean up the test environment.

Run the test suite via the shortcut in the editor or use Maven command `mvnw test`.
If you implement the method exactly as written in this document, the test case execution will fail.
That is the way how test-driven development is conducted.
We always (or, strive to) start with writing a test case that initially fails.

## Pass the First Test

According to the first test case, it expects the controller to use a Web page template (or often called as "view") named `hello` to generate the HTML response.
Therefore, if you want to make the test pass, you need to make an HTML view with the same name.
Now, create a new HTML view in `templates` directory.
The content can be empty, since we only want to make the test pass.
But at least, write something in the HTML such as a "Hello, World" message.

At this point, we can verify that the controller works fine as an independent unit.
The implementation of `showHello` returns the correct string value as we expected.
However, we still do not know if `showHello` will behave as intended when it has to handle an actual HTTP request.
This is where we are going to test the implementation in a mock/simulated environment.

## Test in a Mock Environment

We know that `showHello` is an HTTP request handler implemented in a controller.
If we want to actually verify if the implementation is correct,
we can test the implementation in a simulated Web server.

Go back to the test code and update `HelloControllerTest` class with the following code snippet:

```java
// New imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// New annotation
@WebMvcTest(HelloController.class)
class HelloControllerTest {
    
    // New instance variable
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void showHello_ok() {
        // Omitted for brevity
    } 
    
    @Test
    void showHello_okResponse() throws Exception {
        mockMvc.perform(get("/hello")).andExpectAll(
            status().isOk(),
            content().contentTypeCompatibleWith(TEXT_HTML),
            content().encoding(UTF_8),
            view().name("hello")
        );
    }
}
```

The addition of `@WebMvcTest` annotation will make the test suite
to be executed by a test runner provided by Spring framework.
It will build and run the production code in a simulated Web server.
Hence, it is possible for the test cases to run against a running application.

For example, the new test case above will use an instance of `MockMvc`,
which automatically injected by the Spring framework during runtime,
to send an HTTP GET request to `/hello` path in the application.
Then, we try to verify the HTTP response given by the application.

Try to run the test suite again, either via the shortcut in the IDE or `mvnw test` command in the shell.
You will see that the test suite does not run instantly as they previously did.
The test runner now runs the production code on a simulated server before executing the test cases.
Once you have verified all test cases pass, save your work as a new Git commit and push it to GitHub:

```shell
# Track all changes in both production and test codes
git add src/test/java
git add src/main/java

# And commit the changes into a single commit 
git commit
```

You have gone through a single TDD cycle.
It starts with writing a test, followed by the implementation to make the test pass.
We have not covered refactoring in a TDD cycle since the production code is still quite simple.

In the next section, you will deploy the app to a Platform-as-a-Service provider and learn how to automate the deployment process.
You will also see how to ensure that the test suite is always executed whenever you pushed commits to GitHub.
Make sure you have created an account on [Heroku](https://www.heroku.com) before continuing to the next section.
