# End of Day 1

Congratulations! You have reached the end of Day 1.

## Agenda for Day 2

You will continue work on the todo list app.
You might have noticed that the current iteration of the app can only contain a single todo list.
Tomorrow you will learn how to complete the todo list functionality by implementing the following features:

-  Update and delete a todo list item
-  Automate test execution using GitHub Actions
-  Create a database migration

Tomorrow you will learn how to implement a multiple todo lists functionality and begin to refactor the current app to use PostgreSQL database.

## Additional Material: Functional Test

The TDD approach that you learned from today's session is a TDD process that starts from the "inside" or the innermost layer of the application.
We often call this approach "inside-out TDD".
We start by developing "unit test" that tests each "unit" (function, class, method) independently.
Afterward, we add tests that verify the integration of units, i.e. "integration test".
And finally, we create tests that verify the correctness of the features in the application, i.e. "functional test".

There is another TDD approach called "outside-in TDD".
As you might have guessed, it started from the outermost layer of the application.
We start by developing the functional tests.
Since the initial implementation will fail due to the tests,
we develop more tests in the lower layer of the application.
Afterward, we began to develop the actual implementation to pass the tests.

You can create a functional test by using a browser automation tool such as [Selenium](https://www.selenium.dev/).
To integrate Selenium with JUnit 5 test runner included in Spring Boot,
you can use a test framework called [Selenide](https://selenide.org/).
It provides higher-level abstraction to Selenium and improves the developer experience in setting up functional test.

To include Selenide to the project, add the following dependency declaration into `pom.xml`:

```xml
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide</artifactId>
    <version>6.7.4</version>
    <scope>test</scope>
</dependency>
```

An example of functional test:

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Smoke Test")
class SmokeTest {
    
    @LocalServerPort
    protected int serverPort;
    
    @Value("${sitodo.baseUrl:http://localhost}")
    private String testBaseUrl;

    @Test
    @DisplayName("Verify text in <title> tag")
    void site_hasTitle() {
        browser = "firefox"; // Or your favourite browser
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
        
        open("/");

        assertTrue(title().contains("Sitodo"), "The browser title was: " + title());
        
        closeWebDriver();
    }
}
```

The example above tries to verify if the title displayed on the Web browser contains "Sitodo" string.
The test runner will open up a Web browser and instruct it to open the Web application at given URL (i.e. `/`).
The test runner then will query the state of Web browser when performing verification.

If you are interested, you can try developing functional tests using Selenium & Selenide.
Implement the tests that simulate user (and browser) actions when opening a todo list and submitting a new todo item!
