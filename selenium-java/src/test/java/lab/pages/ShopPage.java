package lab.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page object for the shop. Compare with playwright/pages/ShopPage.ts. */
public class ShopPage {
  private final WebDriver driver;
  private final WebDriverWait wait;

  private static By testId(String id) { return By.cssSelector("[data-testid='" + id + "']"); }

  public ShopPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  /** Types a search and opens the first suggestion's product page. */
  public void openFirstSuggestion(String text) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(testId("search-input"))).sendKeys(text);
    wait.until(ExpectedConditions.elementToBeClickable(testId("search-suggestion"))).click();
  }

  public String productTitle() {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(testId("product-title"))).getText();
  }

  /** Adds the open product to the cart and waits until the header count shows it. */
  public void addToCart(String expectedCount) {
    wait.until(ExpectedConditions.elementToBeClickable(testId("add-to-cart"))).click();
    wait.until(ExpectedConditions.textToBe(testId("cart-count"), expectedCount));
  }

  public String cartCount() {
    return driver.findElement(testId("cart-count")).getText();
  }
}
