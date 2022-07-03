package com.example.sitodo;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        LOG.debug("Initializing Firefox");
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

        assertEquals("Sitodo", title, "Browser title was " + title);
    }

    @AfterEach
    void tearDown() {
        LOG.debug("Terminating Firefox");
        driver.close();
    }
}
