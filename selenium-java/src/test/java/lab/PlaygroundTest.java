package lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** The three things that trip up Selenium suites most: waiting, iframes and shadow DOM. */
class PlaygroundTest extends BaseTest {

  @Test
  void dynamicLoadingWaitsForTheResult() {
    open("playground");
    click(testId("dynamic-start"));
    // Never Thread.sleep(): wait for the condition. Playwright's expect(...).toHaveText() does this for you.
    wait.until(ExpectedConditions.textToBe(testId("dynamic-result"), "Hello, tester!"));
  }

  @Test
  void iframeForm() {
    open("playground");
    // Selenium: switch into the frame, work, switch back out. Forget to switch back and the next step fails.
    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(testId("frame-newsletter")));
    visible(testId("frame-email")).sendKeys("tester@example.com");
    click(testId("frame-submit"));
    assertEquals("Subscribed: tester@example.com", visible(testId("frame-message")).getText());
    driver.switchTo().defaultContent();
  }

  @Test
  void shadowDomInput() {
    open("playground");
    // Selenium: get the shadow root, then search inside it (CSS selectors only). Playwright just chains.
    SearchContext shadow = visible(testId("shadow-host")).getShadowRoot();
    shadow.findElement(testId("shadow-input")).sendKeys("SAVE10");
    shadow.findElement(testId("shadow-button")).click();
    assertEquals("Shadow input: SAVE10", visible(testId("out-shadow")).getText());
  }
}
