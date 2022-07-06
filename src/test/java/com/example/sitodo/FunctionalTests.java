package com.example.sitodo;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Functional Tests/End-to-End (E2E) Tests")
@Tag("e2e")
class FunctionalTests {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionalTests.class);

    @LocalServerPort
    private int serverPort;

    /**
     * Reads $CI environment variable to see if the test is running on CI server or not.
     * Usually, $CI is set to true when run on GitLab Runner.
     */
    @Value("${ci:false}")
    private boolean isCI;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        LOG.debug("Initializing the Web browser");
        WebDriverManager.firefoxdriver().setup();

        // Run the tests headless if run on non-CI machine, e.g. your PC
        LOG.info("Is Firefox running on CI? {}", isCI ? "Yes" : "No");
        FirefoxOptions options = new FirefoxOptions()
            .setHeadless(isCI);

        driver = new FirefoxDriver(options);
    }

    @Test
    @DisplayName("Verify page title")
    void pageTitle_hasValue() {
        driver.get("http://localhost:" + serverPort);

        String title = driver.getTitle();

        assertEquals("Sitodo", title, "The browser title was " + title);
    }

    @Test
    @DisplayName("User Story 1: Add items into the list")
    void userStory_addItems() {
        // User browses to the app
        driver.get("http://localhost:" + serverPort);

        // User sees the title of the app
        String pageTitle = driver.getTitle();
        String headerTitle = driver.findElement(By.tagName("h1")).getText();

        assertEquals("Sitodo", pageTitle, "The browser title was " + pageTitle);
        assertEquals("Your Todo List", headerTitle, "The header text was " + headerTitle);

        // User is prompted to enter an item
        WebElement inputField = driver.findElement(By.id("id_new_item"));
        String placeholderText = inputField.getAttribute("placeholder");

        assertEquals("Enter an item", placeholderText, "The placeholder text was " + placeholderText);

        // User types an item into an input text box, e.g. "Buy milk"
        inputField.sendKeys("Buy milk");

        // User hits Enter
        inputField.sendKeys(Keys.ENTER);

        // The page updates and displays the newly inserted item
        List<WebElement> items = new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.numberOfElementsToBe(By.tagName("tr"), 1));

        assertEquals(1, items.size(), "There are " + items.size() + " items in the rendered list");
        assertEquals("Buy milk", items.get(0).getText());

        // User types another item into the input text box, e.g. "Buy coffee beans"
        inputField = driver.findElement(By.id("id_new_item"));
        inputField.sendKeys("Buy coffee beans");

        // User hits Enter
        inputField.sendKeys(Keys.ENTER);

        // The page updates again showing both items
        items = new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.numberOfElementsToBe(By.tagName("tr"), 2));

        assertEquals(2, items.size(), "There are " + items.size() + " items in the rendered list");

        // User sees the unique URL assigned to his/her list of items
        String currentUrl = driver.getCurrentUrl();

        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);

        // User tries to access the unique URL
        driver.get(currentUrl);

        // User sees his/her list containing the same items previously inserted
        items = new WebDriverWait(driver, Duration.ofSeconds(3))
            .until(ExpectedConditions.numberOfElementsToBe(By.tagName("tr"), 2));

        assertEquals(2, items.size(), "There are " + items.size() + " items in the rendered list");
    }

    @AfterEach
    void tearDown() {
        LOG.debug("Shutting down the Web browser");
        driver.quit();
    }
}
