package com.example.sitodo.service.impl;

import com.example.sitodo.service.MotivationMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@Tag("unit")
@SpringBootTest
class MotivationMessageServiceImplTest {

    @Value("${sitodo.motivation.empty}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.allFinished}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.halfFinished}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.fewItems}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Autowired
    private MotivationMessageService motivationMessageService;

    @Test
    @DisplayName("Given an empty list, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_emptyList() {
        String message = motivationMessageService.computeMotivationMessage(0, 0);

        assertThat(message, containsString(emptyListMessage));
    }

    @Test
    @DisplayName("Given a list with few items all unfinished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_noFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 0);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_allFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 4);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_halfFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 2);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with few items and single item finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_fewItems_singleFinished() {
        String message = motivationMessageService.computeMotivationMessage(4, 1);

        assertThat(message, allOf(
            containsString(fewItemsMessage),
            containsString(someFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and none finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_noFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, 0);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(noFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and all finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_allFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, items.length);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and half finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_halfFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, items.length / 2);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    @Test
    @DisplayName("Given a list with many items and single finished, computeMotivationMessage should produce the correct message")
    void computeMotivationMessage_manyItems_singleFinished() {
        String[] items = IntStream
            .range(0, manyItemsThreshold * 2)
            .mapToObj(i -> "Task " + i)
            .toArray(String[]::new);

        String message = motivationMessageService.computeMotivationMessage(items.length, 1);

        assertThat(message, allOf(
            containsString(manyItemsMessage),
            containsString(someFinishedMessage)
        ));
    }
}
