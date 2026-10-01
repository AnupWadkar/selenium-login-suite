package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Centralizes WebDriver creation so every test class shares the same setup,
 * timeouts, and teardown logic instead of duplicating driver code everywhere.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        // Uncomment to run headless (useful once you wire this into CI):
        // options.addArguments("--headless=new");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driverThread.set(driver);
    }

    public static WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThread.remove();
        }
    }
}
