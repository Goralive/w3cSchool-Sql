package com.w3c.ui.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.w3c.ui.core.BrowserFactory.*;

public class Element {
    private By by;
    private String name;

    public Element(By by, String name) {
        this.by = by;
        this.name = name;
    }

    public Element(By by) {
        this(by, "");
    }

    public WebElement find() {
        return getWebDriverWait(10).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public Actions actions() {
        return new Actions(driver());
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver()).executeScript(script, args);
    }

    public void typeByJsToEditor(String value) {
        executeScript("window.editor.setValue('" + value + "')");
    }

    public void click() {
        find().click();
    }

    public void type(String text) {
        find().clear();
        find().sendKeys(text);
    }

    public boolean isElementPresent() {
        try {
            getWebDriverWait(10).until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementClickable() {
        try {
            getWebDriverWait(10).until(ExpectedConditions.elementToBeClickable(by));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void sendKeys(CharSequence... keysToSend) {
        find().sendKeys(keysToSend);
    }

    public void typeByChar(String text) {
        find().clear();
        for (Character character : text.toCharArray()) {
            find().sendKeys(character.toString());
        }
    }
}
