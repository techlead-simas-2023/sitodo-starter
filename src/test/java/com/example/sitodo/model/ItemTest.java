package com.example.sitodo.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ItemTest {

    @Test
    void testEquals() {
        Item first = new Item("Buy milk");
        Item second = new Item("Cut grass");

        assertNotEquals(first, second);
    }

    @Test
    void testHashCode() {
        Item first = new Item("Buy milk");
        Item second = new Item("Cut grass");

        assertNotEquals(first.hashCode(), second.hashCode());
    }
}
