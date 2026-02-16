# iOS TAF (Appium + TestNG) for Sauce Labs

## What’s inside
- Appium Java client + TestNG
- Page Object pattern (Springboard / Contacts)
- Logging via SLF4J (configured with `simplelogger.properties`)
- Sauce job status is marked as `passed/failed` (best-effort)

## How to run

### 1) Provide Sauce credentials (recommended: ENV)

**JVM properties**
```bash
mvn test -Dsaucelabs.username="..." -Dsaucelabs.accessKey="..."
```

### 2) Run tests
```bash
mvn test
```

