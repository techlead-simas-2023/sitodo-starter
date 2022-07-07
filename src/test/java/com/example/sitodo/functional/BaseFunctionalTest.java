package com.example.sitodo.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseFunctionalTest.class);

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

    private static WebDriver initFirefoxDriver(boolean isCI) {
        WebDriverManager.firefoxdriver().setup();

        LOG.info("Is Firefox running on CI? {}", isCI);
        FirefoxOptions options = new FirefoxOptions()
            .setHeadless(isCI);

        return new FirefoxDriver(options);
    }
}
