package com.example.sitodo.functional;

import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("User Story 2: Update Todo Item")
@Tag("e2e")
class UpdateTodoItemTest extends BaseFunctionalTest {

    @Test
    @DisplayName("A user can create a single todo item and mark it as finished")
    void addSingleTodoItem_thenFinishIt() {
        open("/");

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
        WebElement markFinishLink = $(By.className("sitodo-finish-link"));
        markFinishLink.click();

        // Check again the item, now marked as finished
        checkItemsInList(List.of("Buy milk"));
        ElementsCollection rows = $(By.tagName("tbody")).findAll(By.tagName("tr"));

        List<WebElement> columns = rows.get(0).findElements(By.tagName("td"));
        String itemId = columns.get(0).getText();
        String title = columns.get(1).getText();
        String status = columns.get(2).getText();

        assertEquals("Buy milk", title);
        assertEquals("Finished", status);

        // The user is curious whether the item's status is still the same even
        // if marked as finished twice
        String currentUrl = webdriver().driver().url();
        String[] urlComponents = currentUrl.split("/");
        String listId = urlComponents[urlComponents.length - 1];
        open(String.format("/list/%s/update/%s?finished=true", listId, itemId));

        checkItemsInList(List.of("Buy milk"));
        rows = $(By.tagName("tbody")).findAll(By.tagName("tr"));

        columns = rows.get(0).findElements(By.tagName("td"));
        title = columns.get(1).getText();
        status = columns.get(2).getText();

        assertEquals("Buy milk", title);
        assertEquals("Finished", status);
    }
}
