# Selenium → Playwright Migration Lab — Student Handout

*A hands-on migration lab. By Avneet Dhanoa (QualityFirstQA LLC).*

Work through this top to bottom. Each step has a **Do** (run something) and a **Check** (what you should see). Tick the checkpoints as you go.

**Time:** ~75 minutes. **You need:** Java 17+ and Maven (for the "before"), Node.js 20+ (for the "after"), Chrome. Java 11+ gets you the Allure reports.

---

## The one idea to hold onto: it's the same play in two languages

This repo is **one test suite written twice** — once in **Selenium + Java + JUnit 5** (the "before"), once in **Playwright + TypeScript** (the "after") — against the same site. Nothing about *what* is tested changes. What changes is *how much you have to say to get it done.*

Reading them side by side is like reading a book and its translation with the dictionary open. The dictionary is **[MIGRATION.md](MIGRATION.md)** — it maps every Selenium concept to its Playwright equivalent: waits, frames, shadow DOM, page objects, API testing.

The lesson isn't "Playwright good, Selenium bad." It's **what disappears in the translation, and why:**

- Explicit `WebDriverWait`/`ExpectedConditions` → **auto-waiting** assertions. Whole lines vanish.
- `switchTo().frame(...)` / `switchTo().defaultContent()` → a **`frameLocator`** you can't forget to exit.
- Manual shadow-DOM piercing → **locators pierce shadow DOM** by default.
- No real API testing → a **`request` fixture** makes API checks a first-class thing.

Your job: run both, then explain each deletion.

---

## Part 1 — Run the "before" (Selenium + Java)

**Do:**
```bash
cd selenium-java
mvn test
```

**Check** ☐ The Selenium suite runs against the live lab and passes. (First run downloads Maven deps — give it a minute.)

☐ Open `src/test/java/lab/CheckoutTest.java` and skim it. Note the **waits** and the **ceremony** — driver setup, explicit waits, frame switching. Don't fix anything; just feel the weight.

---

## Part 2 — Run the "after" (Playwright + TypeScript)

**Do:**
```bash
cd ../playwright
npm install
npx playwright install chromium
npx playwright test
```

**Check** ☐ The Playwright suite runs the **same tests** and passes.

☐ Open `specs/checkout.spec.ts` next to the Java `CheckoutTest.java`. Same journey. Read both top to bottom.

> Both use the live lab by default; set `LAB_URL=http://localhost:8788` for a local copy.

---

## Part 3 — Read them side by side (the core of the lab)

Open each pair and find the **exact lines that disappeared** in the translation. Use [MIGRATION.md](MIGRATION.md) as your dictionary.

| Test | Selenium (before) | Playwright (after) | What to look for |
|---|---|---|---|
| Search & open a product | `ShopTest.searchAndOpenAProduct` | `shop.spec.ts` | explicit wait → auto-wait |
| Add to cart | `ShopTest.addAProductToTheCart` | `shop.spec.ts` | fewer setup lines |
| Checkout end to end | `CheckoutTest.checkoutEndToEnd` | `checkout.spec.ts` | the whole shape |
| Dynamic content | `PlaygroundTest.dynamicLoadingWaitsForTheResult` | `playground.spec.ts` | **the wait is gone** |
| Form inside an iframe | `PlaygroundTest.iframeForm` | `playground.spec.ts` | `switchTo` → `frameLocator` |
| Shadow DOM | `PlaygroundTest.shadowDomInput` | `playground.spec.ts` | manual piercing → none |
| API checks | *n/a* | `api.spec.ts` | **only possible after** |

☐ For the **dynamic content** test: what did the Selenium version have to *say* that the Playwright version doesn't, and why is the Playwright one still correct (not just shorter)? _______

☐ Why is there **no API test** in the Selenium column at all? _______

---

## Part 4 — The same report from both stacks

Both suites write **Allure** results, so you can compare the runs in the same format.

**Do (Selenium):**
```bash
cd selenium-java
mvn test
npx allure-commandline generate target/allure-results -o target/allure-report
# open target/allure-report/index.html
```

**Do (Playwright):**
```bash
cd ../playwright
npx playwright test
npx allure-commandline generate allure-results -o allure-report
# open allure-report/index.html
```

**Check** ☐ Open both Allure reports side by side. The Allure CLI needs Java 11+. In CI, both are saved as artifacts on every run.

---

## Part 5 — Feel why the migration matters (the iframe trap)

This is the most convincing moment in the lab.

**Do:** in `PlaygroundTest.java`, **remove** `switchTo().defaultContent()` after the iframe form, and add a step that interacts with something **outside** the iframe right after. Run it.

**Check** ☐ It breaks — Selenium is still "inside" the frame and can't find the outside element.

☐ Now look at the Playwright `frameLocator` version. **Why can that exact mistake not happen there?** _______

> This is the point of the whole repo in one bug: Playwright removes a class of error, it doesn't just remove typing. That's the argument you'd make to a team deciding whether to migrate.

---

## Part 6 — Your turn (exercises)

1. **Count the lines** in `CheckoutTest.java` and `checkout.spec.ts`. Which lines disappeared, and why? (Group them: waits, setup, frame switching.) _______
2. Add a **"remove from cart"** test to the Selenium suite, then **migrate it** to Playwright. Note what you didn't have to write the second time.
3. Reproduce the **iframe trap** from Part 5 cleanly and write up, in two sentences, why the mistake is structurally impossible in Playwright.
4. **Migrate `PlaygroundTest` with an AI assistant**, then review its output against [MIGRATION.md](MIGRATION.md). **What did it get wrong?** (AI is good at line-for-line translation and bad at knowing which lines should vanish.) _______

---

## Done — what you should be able to say now

Tick each once you can explain it to the person next to you:

- ☐ What **auto-waiting** replaces, and why the shorter test is still correct.
- ☐ Why **`frameLocator`** removes a whole class of bug that `switchTo()` invites.
- ☐ Why **API testing** becomes first-class only after the migration.
- ☐ How to use **[MIGRATION.md](MIGRATION.md)** to justify a migration concept by concept to a team.
- ☐ Where an **AI assistant helps** a migration and where it quietly gets it wrong.

---

*Part of a five-repo portfolio — start at [practice-lab](https://github.com/dhanoapartners/practice-lab). Built on [Practice Lab](https://www.dhanoapartners.com). All data synthetic.*
