package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DriverFactory;

/**
 * Login test suite for https://www.saucedemo.com/
 *
 * Test data is deliberately chosen to cover equivalence classes and boundary
 * conditions rather than just "happy path + one failure":
 *   - valid credentials (positive class)
 *   - a recognized-but-locked account (a distinct business-rule class,
 *     not just "wrong password")
 *   - wrong username, wrong password (two separate negative classes -
 *     don't assume they hit the same code path)
 *   - empty username, empty password, both empty (boundary: the 0-length case)
 */
public class LoginTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test(description = "Valid credentials should land the user on the inventory page")
    public void validLogin_shouldReachInventoryPage() {
        var inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isLoaded(), "Expected to land on the inventory page after a valid login");
    }

    @Test(description = "A locked-out user should be blocked with a specific error, not a generic one")
    public void lockedOutUser_shouldShowLockedOutError() {
        loginPage.loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Expected an error message for a locked-out user");
        Assert.assertTrue(
                loginPage.getErrorText().contains("locked out"),
                "Error message should specifically mention the account is locked out"
        );
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][]{
                // username,        password,        expected error fragment
                {"wrong_user",      "secret_sauce",  "do not match"},
                {"standard_user",   "wrong_password","do not match"},
                {"",                "secret_sauce",  "Username is required"},
                {"standard_user",   "",              "Password is required"},
                {"",                "",              "Username is required"},
        };
    }

    @Test(dataProvider = "invalidLoginData",
          description = "Invalid, missing, or mismatched credentials should all be rejected with the correct message")
    public void invalidLogin_shouldShowExpectedError(String username, String password, String expectedErrorFragment) {
        loginPage.loginAs(username, password);
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Expected an error message for input: [" + username + " / " + password + "]");
        Assert.assertTrue(
                loginPage.getErrorText().contains(expectedErrorFragment),
                "Expected error to contain '" + expectedErrorFragment + "' but got: " + loginPage.getErrorText()
        );
    }
}
