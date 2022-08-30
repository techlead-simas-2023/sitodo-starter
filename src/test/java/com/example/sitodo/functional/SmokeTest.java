package com.example.sitodo.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;

@DisplayName("Smoke Test")
@Tag("e2e")
class SmokeTest extends BaseFunctionalTest {

    @Test
    @DisplayName("Verify text in <title> tag")
    void site_hasTitle() {
        open("/");

        assertTrue(title().contains("Sitodo"), "The browser title was: " + title());
    }
}
