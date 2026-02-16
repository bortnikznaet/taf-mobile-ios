package com.taf.ios.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class SauceLabsPropertiesReader {
    private static final String PROPERTIES_FILE = "saucelabs.properties";
    private static final Logger log = LoggerFactory.getLogger(SauceLabsPropertiesReader.class);
    private final Properties properties;

    public SauceLabsPropertiesReader() {
        this.properties = loadProperties();
    }

    public String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String envValue = getFromEnv(key);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String value = properties.getProperty(key);
        return value == null ? null : stripQuotes(value.trim());
    }

    public String getRequired(String key) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Required property is missing: " + key + "\n" +
                            "Provide it via one of these options:\n" +
                            "  - JVM:  -D" + key + "=...\n" +
                            "  - ENV:  " + toEnvKey(key) + "=...\n" +
                            "  - FILE: src/main/resources/" + PROPERTIES_FILE
            );
        }
        return value;
    }

    public String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private Properties loadProperties() {
        Properties loaded = new Properties();

        try (InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (stream == null) {
                throw new IllegalStateException(PROPERTIES_FILE + " was not found in classpath resources");
            }
            loaded.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read " + PROPERTIES_FILE, e);
        }

        return loaded;
    }

    private String getFromEnv(String key) {
        String generic = System.getenv(toEnvKey(key));
        if (generic != null && !generic.isBlank()) {
            return generic;
        }

        switch (key) {
            case "saucelabs.username":
                return firstNonBlank(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_USER"));
            case "saucelabs.accessKey":
                return firstNonBlank(System.getenv("SAUCE_ACCESS_KEY"), System.getenv("SAUCE_KEY"));
            case "saucelabs.url":
                return firstNonBlank(System.getenv("SAUCE_URL"), System.getenv("SAUCELABS_URL"));
            default:
                return null;
        }
    }

    private String toEnvKey(String key) {
        return key.toUpperCase(Locale.ROOT).replace('.', '_');
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }

    private String stripQuotes(String value) {
        if ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
