/** Migrated from ShopTest.java. */
import { test, expect } from '@playwright/test';
import { ShopPage } from '../pages/ShopPage';

test('search and open a product', async ({ page }) => {
  await page.goto('/?ui=v1#shop');
  const shop = new ShopPage(page);
  await shop.openFirstSuggestion('speaker');
  await expect(shop.productTitle()).toHaveText('Bluetooth Speaker');
});

test('add a product to the cart', async ({ page }) => {
  await page.goto('/?ui=v1#shop');
  const shop = new ShopPage(page);
  await shop.openFirstSuggestion('speaker');
  await shop.addToCart('1');
  await expect(shop.cartCount()).toHaveText('1');
});
