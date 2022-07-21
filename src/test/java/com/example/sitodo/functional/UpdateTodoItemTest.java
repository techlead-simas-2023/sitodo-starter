package com.example.sitodo.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("User Story 2: Update Todo Item")
@Tag("e2e")
class UpdateTodoItemTest extends BaseFunctionalTest {

    @Test
    @DisplayName("A user can create a single todo item and mark it as finished")
    void addSingleTodoItem_thenFinishIt() throws Exception {
        driver.get(createBaseUrl("localhost", serverPort));

        // Create a new item and check whether it was successfully saved
        postNewTodoItem("Buy milk");
        checkItemsInList(List.of("Buy milk"));

        // Note: It is getting tedious to use internal methods to verify items in the
        // list. Due to this user story, we find out that we also need to check value
        // in different column (i.e. the status and the link) beside the task's title.
        // The list item verification method might need some refactoring in the future.
        //
        // For instance, if we want to verify columns in a row, maybe we can verify them
        // like using Hamcrest's Matchers:
        //
        // WebElement row = driver.findElement(By.tagName("tr"));
        // assertThatColumn(row,
        //   () -> row[0].getText().matches("\\d+"),
        //   () -> row[1].getText().equals("Buy milk"),
        //   () -> row[2].getText().equals("Not Finished")
        // );
        //
        // I guess it looks better if we can allow verification procedures (strategy)
        // can be given into the assertion method dynamically.

        // Mark the item as finished
        WebElement markFinishLink = driver.findElement(By.className("sitodo-finish-link"));
        markFinishLink.click();

        // Check again the item, now marked as finished
        checkItemsInList(List.of("Buy milk"));
        List<WebElement> rows = new WebDriverWait(driver, DEFAULT_WAIT)
            .until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.tagName("tbody"), By.tagName("tr")));

        List<WebElement> columns = rows.get(0).findElements(By.tagName("td"));
        String itemId = columns.get(0).getText();
        String title = columns.get(1).getText();
        String status = columns.get(2).getText();

        assertEquals("Buy milk", title);
        assertEquals("Finished", status);

        // The user is curious whether the item's status is still the same even
        // if marked as finished twice
        String currentUrl = driver.getCurrentUrl();
        String[] urlComponents = currentUrl.split("/");
        String listId = urlComponents[urlComponents.length - 1];
        driver.get(createBaseUrl("localhost", serverPort)
            + String.format("/list/%s/update/%s?finished=true", listId, itemId)
        );

        checkItemsInList(List.of("Buy milk"));
        rows = new WebDriverWait(driver, DEFAULT_WAIT)
            .until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.tagName("tbody"), By.tagName("tr")));

        columns = rows.get(0).findElements(By.tagName("td"));
        title = columns.get(1).getText();
        status = columns.get(2).getText();

        assertEquals("Buy milk", title);
        assertEquals("Finished", status);
    }
}
