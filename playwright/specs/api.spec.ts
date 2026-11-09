/**
 * Bonus: what you gain after migrating. Selenium can't call APIs; Java teams add REST Assured.
 * Playwright tests the API in the same suite, with the same runner and report.
 */
import { test, expect } from '@playwright/test';

test('the speaker costs $59 in the API', async ({ request }) => {
  const r = await request.get('/api/v1/products/p-1017');
  expect(r.status()).toBe(200);
  expect(await r.json()).toMatchObject({ name: 'Bluetooth Speaker', price: 59 });
});

test('seed the cart through the API, check it in the UI', async ({ page }) => {
  await page.goto('/?ui=v1#hub');
  await expect.poll(async () => (await page.context().cookies()).some((c) => c.name === 'lab_sid')).toBe(true);
  expect((await page.request.post('/api/v1/cart/items', { data: { productId: 'p-1017', qty: 2 } })).status()).toBe(201);
  await page.goto('/?ui=v1#cart');
  await expect(page.getByTestId('line-name')).toHaveText('Bluetooth Speaker');
});
