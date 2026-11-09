import { expect, type Page } from '@playwright/test';

/** Page object for the shop. Compare with selenium-java/.../pages/ShopPage.java: no driver, no waits. */
export class ShopPage {
  constructor(private readonly page: Page) {}

  readonly productTitle = () => this.page.getByTestId('product-title');
  readonly cartCount = () => this.page.getByTestId('cart-count');

  /** Types a search and opens the first suggestion's product page. */
  async openFirstSuggestion(text: string) {
    await this.page.getByTestId('search-input').fill(text);
    await this.page.getByTestId('search-suggestion').first().click();
  }

  /** Adds the open product to the cart and waits until the header count shows it. */
  async addToCart(expectedCount: string) {
    await this.page.getByTestId('add-to-cart').click();
    await expect(this.cartCount()).toHaveText(expectedCount);
  }
}
