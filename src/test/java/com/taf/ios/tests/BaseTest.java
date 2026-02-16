package com.taf.ios.tests;

import com.taf.ios.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseTest {
    protected IOSDriver driver;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        String testName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        log.info("START TEST: {}", testName);
        driver = DriverManager.createDriver(testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
        boolean passed = result.isSuccess();

        try {
            if (!passed) {
                log.error("TEST FAIL: {}", testName, result.getThrowable());
            } else {
                log.info("TEST PASS: {} ", testName);
            }

            try {
                String jobStatus = passed ? "passed" : "failed";
                driver.executeScript("sauce:job-result=" + jobStatus);
            } catch (Exception e) {
                log.debug("Unable to set Sauce job status", e);
            }
        } finally {
            DriverManager.quitDriver();
        }
    }
}
