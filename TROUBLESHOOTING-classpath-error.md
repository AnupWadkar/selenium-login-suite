# Fixing "Cannot find class in classpath: tests.LoginTest"

This error means TestNG successfully parsed `testng.xml`, but couldn't find a
**compiled** `tests.LoginTest` class on the classpath. It's almost always a
project-setup issue, not a bug in the test code itself. Work through these in
order.

---

## 1. Confirm the files are in the right place

In IntelliJ's Project panel, you should see exactly this layout:

```
your-project/
├── pom.xml
├── testng.xml
└── src/test/java/
    ├── pages/
    │   ├── LoginPage.java
    │   └── InventoryPage.java
    ├── tests/
    │   └── LoginTest.java
    └── utils/
        └── DriverFactory.java
```

If `LoginTest.java` isn't physically sitting under `src/test/java/tests/`,
that's the fix — move it there.

---

## 2. Make sure `src/test/java` is marked as a Test Sources Root

If the project was created manually instead of via a Maven archetype,
IntelliJ may not recognize `src/test/java` as a source folder — it'll show
as a plain **gray** folder instead of **green**.

**Fix:** Right-click `src/test/java` → **Mark Directory as** → **Test Sources Root**

---

## 3. Reload Maven

If `pom.xml` was added after the project already existed, or dependencies
changed, IntelliJ may not have picked it up yet.

**Fix:** Open the **Maven** tool window (right sidebar) → click the circular
refresh icon ("Reload All Maven Projects")
*or* right-click `pom.xml` → **Maven** → **Reload Project**

---

## 4. Actually build the project

TestNG needs compiled `.class` files, not just `.java` source — running the
XML suite directly doesn't always trigger a build first. If `LoginTest.java`
fails to compile (e.g. an import error), it silently won't exist on the
classpath, and TestNG throws exactly this error with no explanation why.

**Fix:** **Build → Build Project** (`Ctrl+F9` on Windows/Linux, `Cmd+F9` on Mac)

Check the **Build** window at the bottom for any red compile errors before
moving on.

---

## 5. Re-run

Right-click `testng.xml` → **Run 'testng.xml'**

---

## Most likely culprits

In order of probability:

1. **Step 2** — `src/test/java` not marked as a Test Sources Root
2. **Step 4** — project was never built, or the build failed silently on a
   compile error

## If it still fails

Check the Build window for red compile errors and note:
- Whether `src/test/java` appears **green** in the project tree
- The exact text of any compile error shown

That narrows it to a specific missing import, dependency, or package
mismatch rather than a classpath-wide issue.
