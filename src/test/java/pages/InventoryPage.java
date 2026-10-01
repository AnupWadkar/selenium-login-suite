package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the post-login inventory page.
 * Its presence is what proves a login actually succeeded.
 */
public class InventoryPage {

    private final WebDriver driver;
    private final By pageTitle = By.className("title");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains("inventory.html")
                && driver.findElement(pageTitle).getText().equalsIgnoreCase("Products");
    }
}
