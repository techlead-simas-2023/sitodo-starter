package com.example.sitodo.bdd.helpers;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

/**
 * A custom interaction class to fill a text input field.
 *
 * Reference: https://serenity-bdd.github.io/docs/screenplay/screenplay_webdriver#interacting-with-elements
 */
public class AddAnItem {

    public static Performable withName(String itemName) {
        return Task
            .where("{0} adds an item with name " + itemName,
                Enter.theValue(itemName) // Uses Serenity's `Enter` interaction class
                     .into(TodoListPage.ITEM_NAME_FIELD)
            )
            .then(Task.where("{0} clicks the enter button",
                Click.on(TodoListPage.ENTER_BUTTON) // Uses Serenity's `CLick` interaction class
            ));
    }
}
