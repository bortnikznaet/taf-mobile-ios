package com.taf.ios.pages.contacts;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.taf.ios.pages.BasePage;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ContactsPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(ContactsPage.class);

    public ContactsPage(IOSDriver driver) {
        super(driver);
    }

    public void waitForContactsHome() {
        log.info("Waiting for Contacts home screen");
        wait.until(visibilityOfElementLocated(AppiumBy.iOSNsPredicateString("name == 'Contacts' AND type == 'XCUIElementTypeNavigationBar'")));
    }

    public boolean isContactDisplayed(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By locator = AppiumBy.iOSNsPredicateString(
                "type == 'XCUIElementTypeCell' AND (name == '" + name + "' OR label == '" + name + "')"
        );
        log.info("Checking that contact '{}' is visible", name);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    }
}
