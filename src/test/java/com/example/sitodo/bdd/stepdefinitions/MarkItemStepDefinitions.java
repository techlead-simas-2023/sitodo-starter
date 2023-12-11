package com.example.sitodo.bdd.stepdefinitions;

import com.example.sitodo.bdd.helpers.TodoListPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.openqa.selenium.By;

import java.util.Optional;

public class MarkItemStepDefinitions {

    @When("{actor} marks {string} as Finished")
    public void marks_item_as_finished(Actor actor, String itemName) {
        Optional<WebElementFacade> foundRow = TodoListPage
            .ITEMS_ROWS.resolveAllFor(actor)
            .stream()
            .filter(element -> element.containsText(itemName))
            .findFirst();

        WebElementFacade row = foundRow.orElseThrow();
        WebElementFacade link = row.findBy(By.className("sitodo-finish-link"));

        actor.attemptsTo(Click.on(link));
    }

    @Then("{actor} sees {string} as Finished")
    public void sees_item_as_finished(Actor actor, String itemName) {
        Optional<WebElementFacade> foundRow = TodoListPage
            .ITEMS_ROWS.resolveAllFor(actor)
            .stream()
            .filter(element -> element.containsText(itemName))
            .findFirst();

        WebElementFacade row = foundRow.orElseThrow();

        actor.attemptsTo(
            Ensure.that(row.getText()).contains(itemName),
            Ensure.that(row.getText()).contains("Finished")
        );
    }
}
