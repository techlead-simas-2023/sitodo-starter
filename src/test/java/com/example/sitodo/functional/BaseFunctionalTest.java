package com.example.sitodo.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseFunctionalTest.class);

    protected static final Duration DEFAULT_WAIT = Duration.ofSeconds(5);
    protected static final int DEFAULT_NUMBER_OF_COLUMNS = 4;

    protected WebDriver driver;

    @LocalServerPort
    protected int serverPort;

    /**
     * Reads $CI environment variable to see if the test is running on CI server or not.
     * Usually, $CI is set to true when run on GitLab Runner.
     */
    @Value("${ci:false}")
    private boolean isCI;

    @BeforeEach
    void setUp() {
        LOG.info("Initializing Web browser");
        driver = initFirefoxDriver(isCI);
    }

    @AfterEach
    void tearDown() {
        LOG.info("Terminating the Web browser");
        driver.quit();
    }

    protected String createBaseUrl(String protocol, String host, int port) {
        return String.format("%s://%s:%d", protocol, host, port);
    }

    protected String createBaseUrl(String host, int port) {
        return createBaseUrl("http", host, port);
    }

    protected void postNewTodoItem(String item) {
        WebElement inputField = new WebDriverWait(driver, DEFAULT_WAIT)
            .until(ExpectedConditions.elementToBeClickable(By.tagName("input")));

        inputField.sendKeys(item, Keys.ENTER);

        try {
            // Introduce artificial delay allowing DOM to be correctly rendered after inserting
            // multiple items consecutively
            Thread.sleep(500);
        } catch (InterruptedException exception) {
            LOG.error("There was a problem during artificial delay", exception);
        }
    }

    protected void checkItemsInList(List<String> expectedItems) {
        try {
            // Introduce artificial delay allowing DOM to be correctly rendered after inserting
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

        rows.forEach(row -> isRowValid(expectedItems, row));
    }

    private void isRowValid(List<String> expectedItems, WebElement row) {
        List<WebElement> columns = row.findElements(By.tagName("td"));

        assertEquals(DEFAULT_NUMBER_OF_COLUMNS, columns.size(),
            "There were " + columns.size() + " columns in a row");

        String id = columns.get(0).getText();
        String title = columns.get(1).getText();

        assertTrue(Pattern.matches("\\d+", id), "ID must be an integer");
        assertTrue(expectedItems.contains(title), title + " was not found in the list");
    }

    private static WebDriver initFirefoxDriver(boolean isCI) {
        WebDriverManager.firefoxdriver().setup();

        LOG.info("Is Firefox running on CI? {}", isCI);
        FirefoxOptions options = new FirefoxOptions()
            .setHeadless(isCI);

        return new FirefoxDriver(options);
    }
}
