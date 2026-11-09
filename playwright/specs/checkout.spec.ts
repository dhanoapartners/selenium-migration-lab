/** Migrated from CheckoutTest.java. Same steps; every wait is built into the action or the assertion. */
import { test, expect } from '@playwright/test';
import { ShopPage } from '../pages/ShopPage';

test('checkout end to end', async ({ page }) => {
  await page.goto('/?ui=v1#shop');
  const shop = new ShopPage(page);
  await shop.openFirstSuggestion('speaker');
  await shop.addToCart('1');

  await page.goto('/?ui=v1#cart');
  await page.getByTestId('checkout-button').click();

  await page.getByTestId('ship-name').fill('Sam Standard');
  await page.getByTestId('ship-email').fill('sam@example.com');
  await page.getByTestId('ship-address').fill('100 Main St');
  await page.getByTestId('ship-city').fill('San Ramon');
  await page.getByTestId('ship-state').selectOption('CA');
  await page.getByTestId('ship-zip').fill('94583');
  await page.getByTestId('shipping-continue').click();

  await page.getByTestId('delivery-standard').check();
  await page.getByTestId('delivery-continue').click();

  await page.getByTestId('pay-card').fill('4242424242424242');
  await page.getByTestId('pay-expiry').fill('1230');
  await page.getByTestId('pay-cvc').fill('123');
  await page.getByTestId('payment-continue').click();

  await page.getByTestId('place-order').click();
  await expect(page.getByTestId('order-number')).toHaveText(/^KM-\d+$/);
});
