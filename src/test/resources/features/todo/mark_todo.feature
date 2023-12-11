Feature: Mark items in the todo list

    Scenario: Mark single item as Finished
        Given Bob is looking at the list
        When he adds "Buy milk" to the list
        And he marks "Buy milk" as Finished
        Then he sees "Buy milk" as an item in the list
        And he sees "Buy milk" as Finished
