package lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lab.pages.ShopPage;
import org.junit.jupiter.api.Test;

class ShopTest extends BaseTest {

  @Test
  void searchAndOpenAProduct() {
    open("shop");
    ShopPage shop = new ShopPage(driver);
    shop.openFirstSuggestion("speaker");
    assertEquals("Bluetooth Speaker", shop.productTitle());
  }

  @Test
  void addAProductToTheCart() {
    open("shop");
    ShopPage shop = new ShopPage(driver);
    shop.openFirstSuggestion("speaker");
    shop.addToCart("1");
    assertEquals("1", shop.cartCount());
  }
}
