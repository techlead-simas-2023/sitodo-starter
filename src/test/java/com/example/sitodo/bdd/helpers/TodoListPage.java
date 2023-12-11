package com.example.sitodo.bdd.helpers;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Page object model of the main todo list page,
 * specifically the significant elements of the todo list page.
 *
 * Reference:
 * https://serenity-bdd.github.io/docs/screenplay/screenplay_webdriver#using-the-target-class
 */
public class TodoListPage extends PageObject {

    public static Target ITEM_NAME_FIELD = Target
        .the("item name field")
        .located(By.id("title"));

    public static Target ENTER_BUTTON = Target
        .the("enter button")
        .located(By.tagName("button"));

    public static Target ITEMS_LIST = Target
        .the("item list")
        .located(By.xpath("//tbody/tr/td[contains(@class, 'todo-item-title')]"));

    public static Target ITEMS_ROWS = Target
        .the("rows in the item list table")
        .located(By.xpath("//tbody/tr"));
}
