package com.taf.ios.tests;

import com.taf.ios.pages.contacts.ContactsPage;
import com.taf.ios.pages.springboard.SpringboardPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class ContactsFieldsTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ContactsFieldsTest.class);

    @Test
    public void shouldOpenContactsAndVerifyContactIsVisible() {
        SpringboardPage springboard = new SpringboardPage(driver);
        springboard.goHome();

        driver.context("NATIVE_APP");
        springboard.openAppByName("Contacts");

        ContactsPage contacts = new ContactsPage(driver);
        contacts.waitForContactsHome();

        log.info("Asserting contact is visible in Contacts list");
        assertTrue("Anna Haro contact not found", contacts.isContactDisplayed("Anna Haro"));
    }
}
