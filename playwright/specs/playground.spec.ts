/** Migrated from PlaygroundTest.java: waiting, iframes and shadow DOM. */
import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => { await page.goto('/?ui=v1#playground'); });

test('dynamic loading waits for the result', async ({ page }) => {
  await page.getByTestId('dynamic-start').click();
  await expect(page.getByTestId('dynamic-result')).toHaveText('Hello, tester!'); // retries until it's true
});

test('iframe form', async ({ page }) => {
  // No switching in or out: frameLocator scopes every locator to the frame.
  const frame = page.frameLocator('[data-testid="frame-newsletter"]');
  await frame.getByTestId('frame-email').fill('tester@example.com');
  await frame.getByTestId('frame-submit').click();
  await expect(frame.getByTestId('frame-message')).toHaveText('Subscribed: tester@example.com');
});

test('shadow DOM input', async ({ page }) => {
  // Playwright pierces open shadow roots, so these are ordinary locators.
  await page.getByTestId('shadow-input').fill('SAVE10');
  await page.getByTestId('shadow-button').click();
  await expect(page.getByTestId('out-shadow')).toHaveText('Shadow input: SAVE10');
});
