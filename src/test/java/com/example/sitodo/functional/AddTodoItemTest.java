package com.example.sitodo.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("User Story 1: Add Todo Item")
@Tag("e2e")
class AddTodoItemTest extends BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(AddTodoItemTest.class);

    private static final Duration DEFAULT_WAIT = Duration.ofSeconds(5);

    @Test
    @DisplayName("An user can create a single todo item")
    void addTodoItem_single() {
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);
    }

    @Test
    @DisplayName("An user can create multiple todo items")
    void addTodoItem_multiple() {
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);

        // Create another item
        postNewTodoItem("Cut grass");

        // See the list again to see the new items
        checkItemsInList(List.of("Buy milk", "Cut grass"));

        // The URL is still the same from previous items
        assertEquals(currentUrl, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("An user can create two todo lists consecutively")
    void addTodoItem_twoUsers() {
        // First list
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        postNewTodoItem("Buy milk");
        checkItemsInList(List.of("Buy milk"));

        String firstUrl = driver.getCurrentUrl();
        assertTrue(firstUrl.matches(".+/list/\\d+$"), "The URL was: " + firstUrl);

        // Second list
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        postNewTodoItem("Buy coffee");
        checkItemsInList(List.of("Buy coffee"));

        String secondUrl = driver.getCurrentUrl();
        assertTrue(secondUrl.matches(".+/list/\\d+$"), "The URL was: " + secondUrl);

        // Ensure first and second list have different URL
        assertNotEquals(firstUrl, secondUrl, "Both lists must not have the same URL");
    }

    private void postNewTodoItem(String item) {
        WebElement inputField = new WebDriverWait(driver, DEFAULT_WAIT)
            .until(ExpectedConditions.elementToBeClickable(By.tagName("input")));

        inputField.sendKeys(item, Keys.ENTER);
    }

    private void checkOverallPageLayout() {
        WebElement heading = driver.findElement(By.tagName("caption"));
        WebElement inputField = driver.findElement(By.tagName("input"));
        String headingText = heading.getText();
        String placeholderText = inputField.getAttribute("placeholder");

        assertEquals("Your Todo List", heading.getText(), "The heading title was: " + headingText);
        assertEquals("Enter an item", inputField.getAttribute("placeholder"), "The placeholder text was: " + placeholderText);
    }

    private void checkItemsInList(List<String> expectedItems) {
        try {
            // Introduce artificial delay to allow DOM to be correctly rendered after inserting
            // multiple items consecutively
            Thread.sleep(500);
        } catch (InterruptedException exception) {
            LOG.error("There was a problem during artificial delay", exception);
        }

        List<WebElement> rows = new WebDriverWait(driver, DEFAULT_WAIT)
            .until(ExpectedConditions.presenceOfNestedElementsLocatedBy(
                By.tagName("tbody"),
                By.tagName("tr"))
            );

        assertEquals(expectedItems.size(), rows.size(), "There were " + rows.size() + " items in the list");

        rows.forEach(row -> {
            List<WebElement> columns = row.findElements(By.tagName("td"));

            assertTrue(
                columns.stream()
                    .map(WebElement::getText)
                    .anyMatch(expectedItems::contains),
                "There were mismatched items"
            );
        });
    }
}
