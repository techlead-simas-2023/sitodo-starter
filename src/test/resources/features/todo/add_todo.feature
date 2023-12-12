Feature: Add items into the todo list
    As a user, I would like to add todo items.

    Scenario: Add single item into the list
        Given Alice is looking at the list
        When she adds "Touch grass" to the list
        Then she sees "Touch grass" as an item in the list

    # TODO: Create a new test scenario that adds multiple items into the list
