package com.taf.ios.pages;

import com.taf.ios.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final IOSDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected IOSDriver getDriver() {
        return driver;
    }
}
