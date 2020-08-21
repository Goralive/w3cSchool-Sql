package regression;

import com.w3c.ui.core.BrowserFactory;
import com.w3c.ui.pages.TrySQLPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.w3c.ui.core.Constants.CUSTOMERS_TABLE;
import static com.w3c.ui.core.Constants.EQUALS_SIGH;

@Epic("Regression SQL CRUD")
@Feature("SQL checks")
public class TrySQLTest extends BrowserFactory {
    TrySQLPage trySQLPage = new TrySQLPage();

    @Story("Verify data for user Giovanni Rovelli")
    @Test
    public void shouldContactNameHaveExpectedAddress() {
        trySQLPage
                .open()
                .selectFrom("*", CUSTOMERS_TABLE);
        trySQLPage.executeSQLQuery();
        Assert.assertTrue(trySQLPage.shouldResultListContainNameWithAddress("Giovanni Rovelli", "Via Ludovico il Moro 22"));
    }

    @Story("Select customers from London")
    @Test
    public void shouldSixCustomersFromLondon() {
        trySQLPage
                .open()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCity(EQUALS_SIGH, "London");
        trySQLPage.executeSQLQuery();
        Assert.assertEquals(trySQLPage.getSizeOfResultArray(), 6);
    }

    @Story("Insert new customer")
    @Test
    public void shouldBeAddedNewCustomer() {
        trySQLPage
                .open()
                .insertInto()
                .addValues("Jackson", "Jackson Ford jr", "Kraken 33", "Mistropol", "3002", "US");
        trySQLPage.executeSQLQuery();

        trySQLPage
                .clearInputFiled()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCustomerName(EQUALS_SIGH, "Jackson");
        trySQLPage.executeSQLQuery();
        Assert.assertTrue(trySQLPage.getSizeOfResultArray() > 0);
    }

    @Story("Update customer")
    @Test
    public void shouldBeUpdatedCustomer() {
        String customerName = "Bobby Brown";
        trySQLPage
                .open()
                .clearInputFiled()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCustomerID(EQUALS_SIGH, 1);
        trySQLPage.executeSQLQuery();

        String beforeUpdateString = trySQLPage.getCustomerName();

        trySQLPage
                .clearInputFiled()
                .update()
                .setNewValuesForCustomer("John", customerName, "Marple str 74", "Detroit", "2002", "US")
                .whereCustomerID(EQUALS_SIGH, 1);
        trySQLPage.executeSQLQuery();

        trySQLPage
                .clearInputFiled()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCustomerID(EQUALS_SIGH, 1);
        trySQLPage.executeSQLQuery();
        Assert.assertTrue(!beforeUpdateString.equals(trySQLPage.getCustomerName())
                && trySQLPage.getCustomerName().equals(customerName));
    }

    @Story("Delete customer Bon app from customer table")
    @Test
    public void shouldInformationAboutContactBeDeleted() {
        //given
        String customerName = "Bon app\\'";
        trySQLPage
                .open()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCustomerName(EQUALS_SIGH, customerName);
        trySQLPage.executeSQLQuery();
        Assert.assertEquals(trySQLPage.getSizeOfResultArray(),1);
        //when
        trySQLPage
                .clearInputFiled()
                .deleteFrom(CUSTOMERS_TABLE)
                .whereCustomerName(EQUALS_SIGH, customerName);
        trySQLPage.executeSQLQuery();
       //then
        trySQLPage
                .clearInputFiled()
                .selectFrom("*", CUSTOMERS_TABLE)
                .whereCustomerName(EQUALS_SIGH, customerName);
        trySQLPage.executeSQLQuery();
        Assert.assertTrue(trySQLPage.isNoResults());
    }
}
