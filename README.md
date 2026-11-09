# Selenium → Playwright migration lab

[![CI](https://github.com/dhanoapartners/selenium-migration-lab/actions/workflows/ci.yml/badge.svg)](https://github.com/dhanoapartners/selenium-migration-lab/actions/workflows/ci.yml)

## What this demonstrates

*By Avneet Dhanoa (QualityFirstQA LLC). Part of a five-repo portfolio; start at [practice-lab](https://github.com/dhanoapartners/practice-lab).*

- **Migration leadership:** the same suite in Selenium + Java + JUnit 5 and in Playwright + TypeScript, so the change is concrete, not theoretical.
- **A concept-by-concept map** ([MIGRATION.md](MIGRATION.md)) a team can follow: waits, frames, shadow DOM, page objects, API testing.
- **Both stacks green in CI**, with Allure reports for each.

The same tests written twice against **[Practice Lab](https://www.dhanoapartners.com)**: once in **Selenium + Java + JUnit 5** (the "before"), once in **Playwright + TypeScript** (the "after"). Open them side by side and see exactly what changes when a team migrates.

| Test | Selenium (before) | Playwright (after) |
|---|---|---|
| Search and open a product | `ShopTest.searchAndOpenAProduct` | `shop.spec.ts` |
| Add to cart | `ShopTest.addAProductToTheCart` | `shop.spec.ts` |
| Checkout end to end | `CheckoutTest.checkoutEndToEnd` | `checkout.spec.ts` |
| Waiting for dynamic content | `PlaygroundTest.dynamicLoadingWaitsForTheResult` | `playground.spec.ts` |
| A form inside an iframe | `PlaygroundTest.iframeForm` | `playground.spec.ts` |
| Shadow DOM | `PlaygroundTest.shadowDomInput` | `playground.spec.ts` |
| API checks (only possible after) | n/a | `api.spec.ts` |

**[MIGRATION.md](MIGRATION.md)** maps every Selenium concept to its Playwright equivalent and walks through a migration step by step.

## Run the Selenium suite (before)

Needs Java 17+, Maven and Chrome.

```bash
cd selenium-java
mvn test
```

## Run the Playwright suite (after)

Needs Node.js 20+.

```bash
cd playwright
npm install
npx playwright install chromium
npx playwright test
```

Both use the live lab by default; set `LAB_URL` to use a local copy. CI runs both suites on every push and every night.

## Allure reports for both

Both suites write Allure results, so the same kind of report comes out of Selenium and Playwright: open them side by side to compare the runs.

- Selenium: `mvn test`, then `npx allure-commandline generate target/allure-results -o target/allure-report` and open `target/allure-report/index.html`.
- Playwright: `npx playwright test`, then `npx allure-commandline generate allure-results -o allure-report`.

In CI, both Allure reports are saved with each run (download them from the run's Artifacts). The Allure CLI needs Java 11+.

## Exercises

1. Count the lines in `CheckoutTest.java` and `checkout.spec.ts`. Which lines disappeared, and why?
2. Add a "remove from cart" test to the Selenium suite, then migrate it.
3. Break the iframe test in Selenium by removing `switchTo().defaultContent()` and adding a step after it. What happens? Why can't that mistake happen in Playwright?
4. Migrate `PlaygroundTest` using an AI assistant, then review its output against the concept map. What did it get wrong?

---

© 2026 QualityFirstQA LLC. Dhanoa Partners is a training brand of QualityFirstQA LLC. For personal learning; see LICENSE.
