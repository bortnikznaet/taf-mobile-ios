package com.taf.ios.pages.springboard;

import com.taf.ios.pages.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class SpringboardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(SpringboardPage.class);

    private static final String SPRINGBOARD_BUNDLE_ID = "com.apple.springboard";

    public SpringboardPage(IOSDriver driver) {
        super(driver);
    }

    public void goHome() {
        try {
            log.info("Going to Home screen via activateApp({})", SPRINGBOARD_BUNDLE_ID);
            driver.activateApp(SPRINGBOARD_BUNDLE_ID);
            return;
        } catch (Exception ignored) {
            log.debug("activateApp() not available on this provider; trying fallbacks");
        }

        try {
            log.info("Going to Home screen via mobile: pressButton (home)");
            driver.executeScript("mobile: pressButton", Map.of("name", "home"));
            return;
        } catch (Exception ignored) {
            log.debug("mobile: pressButton not available; trying swipe fallback");
        }

    }

    public void openAppByName(String appName) {
        log.info("Opening app from Springboard: {}", appName);
        WebElement icon = wait.until(elementToBeClickable(AppiumBy.accessibilityId(appName)));
        icon.click();
    }

}
