package com.example.sitodo.bdd.stepdefinitions;

import com.example.sitodo.bdd.helpers.AddAnItem;
import com.example.sitodo.bdd.helpers.NavigateTo;
import com.example.sitodo.bdd.helpers.TodoListPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;

import java.util.List;

public class AddItemStepDefinitions {

    @Given("{actor} is looking at the list")
    public void actor_is_looking_at_her_todo_list(Actor actor) {
        actor.wasAbleTo(NavigateTo.theTodoListPage());
    }

    @When("{actor} adds {string} to the list")
    public void she_adds_to_the_list(Actor actor, String itemName) {
        actor.attemptsTo(AddAnItem.withName(itemName));
    }

    @Then("{actor} sees {string} as an item in the list")
    public void she_sees_as_an_item_in_the_todo_list(Actor actor, String expectedItemName) {
        List<String> todoItems = TodoListPage.ITEMS_LIST.resolveAllFor(actor)
                                                        .textContents();

        actor.attemptsTo(
            Ensure.that(todoItems)
                  .contains(expectedItemName)
        );
    }
}
