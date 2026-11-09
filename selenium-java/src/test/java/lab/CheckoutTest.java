package lab;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lab.pages.ShopPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

class CheckoutTest extends BaseTest {

  @Test
  void checkoutEndToEnd() {
    open("shop");
    ShopPage shop = new ShopPage(driver);
    shop.openFirstSuggestion("speaker");
    shop.addToCart("1");

    open("cart");
    click(testId("checkout-button"));

    visible(testId("ship-name")).sendKeys("Sam Standard");
    driver.findElement(testId("ship-email")).sendKeys("sam@example.com");
    driver.findElement(testId("ship-address")).sendKeys("100 Main St");
    driver.findElement(testId("ship-city")).sendKeys("San Ramon");
    new Select(driver.findElement(testId("ship-state"))).selectByValue("CA");
    driver.findElement(testId("ship-zip")).sendKeys("94583");
    // The "Added to cart" toast can still be covering the button: Selenium would throw
    // ElementClickInterceptedException. Wait for it to go. (Playwright waits for this automatically.)
    wait.until(ExpectedConditions.invisibilityOfElementLocated(testId("toast")));
    click(testId("shipping-continue"));

    click(testId("delivery-standard"));
    click(testId("delivery-continue"));

    visible(testId("pay-card")).sendKeys("4242424242424242");
    driver.findElement(testId("pay-expiry")).sendKeys("1230");
    driver.findElement(testId("pay-cvc")).sendKeys("123");
    click(testId("payment-continue"));

    click(testId("place-order"));
    wait.until(ExpectedConditions.textMatches(testId("order-number"), java.util.regex.Pattern.compile("^KM-\\d+$")));
    assertTrue(driver.findElement(testId("order-number")).getText().startsWith("KM-"));
  }
}
