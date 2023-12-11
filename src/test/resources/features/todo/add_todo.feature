Feature: Add items into the todo list

    Scenario: Add single item into the list
        Given Alice is looking at the list
        When she adds "Touch grass" to the list
        Then she sees "Touch grass" as an item in the list

    Scenario: Add multiple items into the list
        Given Alice is looking at the list
        When she adds "Buy bread" to the list
        And she adds "Buy candy" to the list
        Then she sees "Buy bread" as an item in the list
        And she sees "Buy candy" as an item in the list
