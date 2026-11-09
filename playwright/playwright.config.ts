import { defineConfig, devices } from '@playwright/test';

/**
 * Everything BaseTest.java does (browser, base URL, waits) lives here or is built in.
 * LAB_URL picks the site (default: the live lab). PW_CHROMIUM_PATH uses an installed Chrome.
 */
export default defineConfig({
  testDir: './specs',
  timeout: 30_000,
  workers: process.env.LAB_URL?.includes('localhost') ? undefined : 2,
  retries: process.env.CI ? 1 : 0,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    baseURL: process.env.LAB_URL || 'https://www.dhanoapartners.com',
    trace: 'retain-on-failure',
    launchOptions: process.env.PW_CHROMIUM_PATH ? { executablePath: process.env.PW_CHROMIUM_PATH } : {},
  },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
});
