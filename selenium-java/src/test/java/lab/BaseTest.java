package lab;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared setup for every Selenium test: one Chrome per test, the lab URL, and an explicit wait.
 *
 * Migration note: in Playwright all of this is the built-in `page` fixture plus `baseURL` in the config,
 * and the waiting is automatic.
 */
public abstract class BaseTest {
  protected static final String BASE =
      System.getenv().getOrDefault("LAB_URL", "https://www.dhanoapartners.com").replaceAll("/$", "");

  protected WebDriver driver;
  protected WebDriverWait wait;

  @BeforeEach
  void startBrowser() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new", "--window-size=1366,900");
    driver = new ChromeDriver(options); // Selenium Manager finds a matching driver
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @AfterEach
  void stopBrowser() {
    if (driver != null) driver.quit();
  }

  /** Opens a lab screen on UI v1, e.g. open("shop") → /?ui=v1#shop. */
  protected void open(String view) {
    driver.get(BASE + "/?ui=v1#" + view);
  }

  /** The lab's test-ID locator. Playwright has this built in: page.getByTestId(...). */
  protected static By testId(String id) {
    return By.cssSelector("[data-testid='" + id + "']");
  }

  /** Waits until the element is visible, then returns it. */
  protected WebElement visible(By by) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  /** Waits until the element can be clicked, then clicks it. */
  protected void click(By by) {
    wait.until(ExpectedConditions.elementToBeClickable(by)).click();
  }
}
