# Selenium → Playwright: the concept map

Every Selenium idea in `selenium-java/` and where it went in `playwright/`. Open the two files side by side as you read.

| Selenium (Java) | Playwright (TypeScript) | What changes |
|---|---|---|
| `new ChromeDriver(options)` in `@BeforeEach`, `driver.quit()` in `@AfterEach` | The built-in `page` fixture | No setup or teardown code. A fresh, isolated browser context per test. |
| Driver binaries, Selenium Manager | `npx playwright install` | Browsers are pinned to the Playwright version, so no driver/browser mismatch. |
| `driver.get(BASE + path)` | `page.goto(path)` with `baseURL` in the config | The base URL lives in one place. |
| `By.cssSelector("[data-testid='x']")` | `page.getByTestId('x')` | Test IDs, roles, labels and text are first-class locators. |
| `driver.findElement(by)` (finds now, or throws) | `page.getByTestId(...)` (a recipe, resolved on use) | Locators are lazy and re-resolved on every action, so no `StaleElementReferenceException`. |
| `WebDriverWait` + `ExpectedConditions` | Auto-waiting actions + `expect(...)` assertions that retry | Most explicit waits disappear. |
| `Thread.sleep(...)` | Nothing | Wait for a state, never a time. |
| `ElementClickInterceptedException` (a toast or overlay covers the button), fixed with an extra wait | Nothing | Playwright waits until the element can actually receive the click. This repo's CI hit exactly this: see the toast wait in `CheckoutTest.java`. |
| `sendKeys(...)` | `fill(...)` | `fill` clears first and waits for the field to be editable. |
| `new Select(el).selectByValue("CA")` | `selectOption('CA')` | |
| `switchTo().frame(...)` … `switchTo().defaultContent()` | `page.frameLocator(...)` | No switching in and out; nothing to forget. |
| `getShadowRoot()` then CSS-only search inside | Ordinary locators | Playwright pierces open shadow roots. |
| JUnit `assertEquals(expected, el.getText())` | `await expect(locator).toHaveText(expected)` | The assertion waits for the text instead of reading it once. |
| Page Factory / page objects holding a `driver` | Page objects holding a `page` | Same pattern, fewer lines (compare the two `ShopPage` files). |
| REST Assured (a second library) for APIs | The `request` fixture | API and UI tests in one suite, runner and report. |
| Selenium Grid for parallel runs | `workers` and `--shard` | Parallel by default on one machine; shard across CI machines. |
| Screenshots on failure via a listener | `trace: 'retain-on-failure'` | A full trace: every action, DOM snapshot, network call and console log. |

## A migration, step by step

1. **Inventory.** List the Selenium tests and group them by flow (shop, checkout, account). Note which are flaky today: they're the first to migrate, because they gain the most.
2. **Freeze the locators you'll keep.** Prefer test IDs and roles. If the Selenium suite uses XPath or layout CSS, decide the new locator now; don't carry the old one over.
3. **Build the base once.** Config (`baseURL`, retries, trace), fixtures and the first page object. In this repo that's `playwright.config.ts` and `pages/ShopPage.ts`.
4. **Migrate one flow end to end.** Shop first. Delete every explicit wait and sleep as you go, and replace read-then-assert with `expect(...)`.
5. **Run both suites side by side** on the same build until the Playwright version is green and stable for a week. That's what CI in this repo does.
6. **Retire the Selenium test** only when its Playwright replacement has been stable. Then move to the next flow.
7. **Use what you gain.** Move test data setup to API calls (see `playwright/specs/api.spec.ts`), turn on traces, run in parallel.

AI can speed up steps 4 and 6 (draft the Playwright version from the Selenium file, then you review it), but the locator decisions in step 2 and the review are still yours.
