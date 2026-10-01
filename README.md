# Selenium Login Test Suite — saucedemo.com

A Page-Object-Model TestNG suite covering login equivalence classes and
boundary cases: valid login, locked-out user, wrong username, wrong
password, and empty-field cases.

## Requirements

- **Java 17+** (`java -version` to check)
- **Maven 3.8+** (`mvn -version` to check)
- **Google Chrome** installed (Selenium Manager auto-downloads the matching
  ChromeDriver — you don't need to install it separately)

## Project structure

```
selenium-login-suite/
├── pom.xml                              # Maven dependencies (Selenium 4.40, TestNG)
├── testng.xml                           # Test suite definition
├── README.md
└── src/test/java/
    ├── pages/
    │   ├── LoginPage.java                # Page Object for the login screen
    │   └── InventoryPage.java            # Page Object for the post-login page
    ├── tests/
    │   └── LoginTest.java                # The actual test cases
    └── utils/
        └── DriverFactory.java            # WebDriver setup/teardown
```

## How to run

### Option A — Command line (fastest way to confirm it works)

```bash
cd selenium-login-suite
mvn clean test
```

This downloads dependencies, compiles everything, and runs the suite
via `testng.xml` (wired up through the Surefire plugin in `pom.xml`).

### Option B — IntelliJ IDEA

1. **File → Open** → select the unzipped `selenium-login-suite` folder
2. Let IntelliJ auto-import the Maven project (or right-click `pom.xml` →
   **Maven → Reload Project**)
3. Confirm `src/test/java` shows **green** in the project tree (it should
   be auto-marked as a Test Sources Root since this is a standard Maven
   layout — if not, right-click it → **Mark Directory as → Test Sources Root**)
4. **Build → Build Project**
5. Right-click `testng.xml` → **Run 'testng.xml'**

## What's being tested

| Test | Input | Expected outcome |
|---|---|---|
| `validLogin_shouldReachInventoryPage` | `standard_user` / `secret_sauce` | Lands on Products/inventory page |
| `lockedOutUser_shouldShowLockedOutError` | `locked_out_user` / `secret_sauce` | Error mentions "locked out" |
| `invalidLogin_shouldShowExpectedError` (data-driven, 5 cases) | wrong username, wrong password, empty username, empty password, both empty | Correct error message per case |

## Customizing

- **Headless mode:** uncomment the `--headless=new` line in `DriverFactory.java`
- **Different site/app:** update the URL in `LoginPage.open()` and the
  locators in `LoginPage.java` / `InventoryPage.java`
- **More test data:** add rows to the `invalidLoginData` `@DataProvider`
  in `LoginTest.java` — no new test method needed

## Troubleshooting

If you hit `Cannot find class in classpath: tests.LoginTest`, it's almost
always one of:
1. `src/test/java` not marked as a Test Sources Root
2. Maven project not reloaded after opening
3. Project never actually built (Build → Build Project) — check for red
   compile errors in the Build window first
