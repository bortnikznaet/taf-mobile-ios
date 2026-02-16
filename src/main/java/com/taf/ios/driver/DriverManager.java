package com.taf.ios.driver;

import com.taf.ios.config.SauceLabsPropertiesReader;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<IOSDriver> DRIVER_HOLDER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static IOSDriver getDriver() {
        IOSDriver driver = DRIVER_HOLDER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver is not initialized. Call DriverManager.createDriver() first.");
        }
        return driver;
    }


    public static IOSDriver createDriver(String testName) {
        SauceLabsPropertiesReader reader = new SauceLabsPropertiesReader();
        XCUITestOptions options = new XCUITestOptions();

        options.setPlatformName(reader.getOrDefault("platform.name", "iOS"));
        options.setDeviceName(reader.getRequired("deviceName"));
        options.setPlatformVersion(reader.getRequired("platformVersion"));
        options.setAutomationName(reader.getOrDefault("automationName", "XCUITest"));

        options.setCapability("browserName",
                reader.getOrDefault("browserName", "Safari"));

        options.setAutoAcceptAlerts(true);
        options.setNewCommandTimeout(Duration.ofSeconds(Long.parseLong(reader.getOrDefault("newCommandTimeout", "600"))));

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("appiumVersion", reader.getOrDefault("sauce.appiumVersion", "2.0.0"));
        sauceOptions.put("username", reader.getRequired("saucelabs.username"));
        sauceOptions.put("accessKey", reader.getRequired("saucelabs.accessKey"));
        sauceOptions.put("build", reader.getOrDefault("build.id", "local-build"));
        sauceOptions.put("name", (testName == null || testName.isBlank())
                ? reader.getOrDefault("test.name", "iOS test")
                : testName);
        sauceOptions.put("deviceOrientation", reader.getOrDefault("deviceOrientation", "PORTRAIT"));

        options.setCapability("sauce:options", sauceOptions);

        URL remoteUrl = getSauceUrl(reader);
        log.info("Creating iOS session on Sauce Labs: device='{}', iOS='{}', browser='{}', url='{}'",
                options.getDeviceName(), options.getPlatformVersion(),
                options.getCapability("browserName"), remoteUrl);

        IOSDriver driver = new IOSDriver(remoteUrl, options);
        DRIVER_HOLDER.set(driver);
        log.info("Session started: sessionId={}", driver.getSessionId());
        return driver;
    }

    public static void quitDriver() {
        IOSDriver driver = DRIVER_HOLDER.get();
        if (driver != null) {
            try {
                log.info("Quitting session: sessionId={}", driver.getSessionId());
            } catch (Exception ignored) {
            }
            driver.quit();
            DRIVER_HOLDER.remove();
        }
    }

    private static URL getSauceUrl(SauceLabsPropertiesReader reader) {
        String rawUrl = reader.getRequired("saucelabs.url");
        try {
            return new URL(rawUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid SauceLabs URL: " + rawUrl, e);
        }
    }
}
