package com.w3c.ui.pages;

import com.w3c.ui.core.Element;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

import static com.w3c.ui.core.BrowserFactory.*;
import static com.w3c.ui.core.Constants.*;

public class TrySQLPage {

    private static final String PATH = "sql/trysql.asp?filename=trysql_select_all";

    private String querySQL = "";

    public Element tryItForm = new Element(By.cssSelector("#tryitform"), "Try it Form");
    public Element sqlTextElm = new Element(By.cssSelector("#tryitform .cm-m-sql"), "Text in the Form");
    public Element windowEditor = new Element(By.cssSelector("window.editor"), "Window editor");
    public Element runBtn = new Element(By.cssSelector("button.w3-green"), "Run sql script button");
    public Element customerNameElm = new Element(By.cssSelector(".w3-table-all tbody tr td:nth-child(3)"),"Customer name");
    private List<WebElement> elementList;

    @Step("Open Url")
    public TrySQLPage open() {
        driver().get(URL + PATH);
        Assert.assertTrue(isOpened(), "Page 'login' [" + PATH + "] not Opened");
        clearInputFiled();
        Assert.assertTrue(isInputFieldClear());
        return this;
    }

    @Step("Clear input field")
    public TrySQLPage clearInputFiled() {
        tryItForm.click();
        tryItForm.actions()
                .keyDown(Keys.CONTROL).sendKeys("a")
                .sendKeys(Keys.DELETE)
                .perform();
        return this;
    }

    @Step("SELECT {0} FROM {1}")
    public TrySQLPage selectFrom(String row, String tableName) {
        querySQL = SELECT + row + FROM + tableName;
        return this;
    }

    @Step("WHERE city {0} {1}")
    public TrySQLPage whereCity(String sign, String data) {
        querySQL += "\\n" + WHERE + CITY + sign + buildData(data);
        return this;
    }

    @Step("WHERE customerName {0} {1}")
    public TrySQLPage whereCustomerName(String sign, String data) {
        querySQL += "\\n" + WHERE + CUSTOMER_NAME + sign + buildData(data);
        return this;
    }

    @Step("WHERE customerID {0} {1}")
    public TrySQLPage whereCustomerID(String sign, int data) {
        querySQL += "\\n" + WHERE + CUSTOMER_ID + sign + data;
        return this;
    }

    public TrySQLPage insertInto() {
        querySQL = INSERT + CUSTOMERS_TABLE + INSERT_NEW_CUSTOMER;
        return this;
    }

    public String getCustomerName(){
       return customerNameElm.find().getText();
    }

    public TrySQLPage addValues(String customerName, String contactName, String address, String city, String postalCode,
                                String country) {
        querySQL += "\\n" + VALUES + "(" + buildData(customerName) + "," + buildData(contactName) + ","
                + buildData(address) + "," + buildData(city) + ","
                + buildData(postalCode) + "," + buildData(country) + ")";
        return this;
    }

    public TrySQLPage update() {
        querySQL = UPDATE + CUSTOMERS_TABLE;
        return this;
    }


    public TrySQLPage setNewValuesForCustomer(String customerName, String contactName, String address, String city, String postalCode,
                                              String country) {
        querySQL += "\\n" + SET + "CustomerName = " + buildData(customerName) + ", ContactName = " + buildData(contactName)
                + ", Address = " + buildData(address) + ", City = " + buildData(city) + ", PostalCode = "
                + buildData(postalCode) + ", Country = " + buildData(country);
        return this;
    }

    public int getSizeOfResultArray() {
        getWebDriverWait(10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".w3-table-all tbody tr:not(:first-child)")));
        return driver().findElements(By.cssSelector(".w3-table-all tbody tr:not(:first-child)")).size();
    }

    public boolean shouldResultListContainNameWithAddress(String contactName, String expectedAddress) {
        getWebDriverWait(10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(".w3-table-all tbody tr")));
        elementList = driver().findElements(By.cssSelector(".w3-table-all tbody tr"));
        for (WebElement elem : elementList) {
            List<WebElement> tempList = elem.findElements(By.cssSelector("td:nth-child(3)"));
            for (WebElement temp : tempList) {
                if (temp.getText().equals(contactName)) {
                    String actualAddress = elem.findElement(By.cssSelector("td:nth-child(4)")).getText();
                    return actualAddress.equals(expectedAddress);
                }
            }
        }
        return false;
    }

    public void executeSQLQuery() {
        windowEditor.typeByJsToEditor(this.querySQL + ";");
        runBtn.click();
    }

    private String buildData(String data) {
        return "\"" + data + "\"";
    }

    @Step()
    private boolean isOpened() {
        return tryItForm.isElementPresent() && driver().getCurrentUrl().contains(PATH);
    }

    @Step
    private boolean isInputFieldClear() {
        return !sqlTextElm.isElementPresent();
    }
}
