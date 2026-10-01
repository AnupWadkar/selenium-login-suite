package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.DriverFactory;

/**
 * Page Object for https://www.saucedemo.com/
 * Keeps locators and actions in one place so tests read like plain English
 * and don't break every time a selector changes.
 */
public class LoginPage {

    private final WebDriver driver;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage open() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    public LoginPage enterUsername(String username) {
        WebElement field = driver.findElement(usernameField);
        field.clear();
        field.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement field = driver.findElement(passwordField);
        field.clear();
        field.sendKeys(password);
        return this;
    }

    public InventoryPage clickLogin() {
        driver.findElement(loginButton).click();
        return new InventoryPage(driver);
    }

    /** Convenience method for the common "fill both fields and submit" case. */
    public InventoryPage loginAs(String username, String password) {
        return enterUsername(username).enterPassword(password).clickLogin();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }

    public String getErrorText() {
        DriverFactory.getWait().until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return driver.findElement(errorMessage).getText();
    }
}
