package com.taf.ios.pages;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final IOSDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(IOSDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected IOSDriver getDriver() {
        return driver;
    }
}
