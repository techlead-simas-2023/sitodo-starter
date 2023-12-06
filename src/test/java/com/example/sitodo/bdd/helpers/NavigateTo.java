package com.example.sitodo.bdd.helpers;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;

/**
 * A custom interaction class to open a page. Specifically, it opens the todo list page.
 *
 * Reference: https://serenity-bdd.github.io/docs/screenplay/screenplay_webdriver#interacting-with-elements
 */
public class NavigateTo {

    public static Performable theTodoListPage() {
        return Task
            .where("{0} opens the Todo list page", Open.browserOn()  // Uses Serenity's `Open` interaction class
                                                       .the(TodoListPage.class));
    }
}
