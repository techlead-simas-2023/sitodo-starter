package com.example.sitodo.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Smoke Test")
@Tag("e2e")
class SmokeTest extends BaseFunctionalTest {

    @Test
    @DisplayName("Verify text in <title> tag")
    void site_hasTitle() {
        driver.get(createBaseUrl("localhost", serverPort));

        String title = driver.getTitle();

        assertTrue(title.contains("Sitodo"), "The browser title was: " + title);
    }
}
