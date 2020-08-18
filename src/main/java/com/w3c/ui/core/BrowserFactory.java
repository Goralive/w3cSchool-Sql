package com.w3c.ui.core;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;


public class BrowserFactory {

    private static WebDriver driver;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeTest
    public void setUp() {
        ChromeDriverManager.getInstance(DriverManagerType.CHROME).setup();
        driver = new ChromeDriver();
        driver().manage().window().maximize();
        log.info("Browser Start");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
        log.info("Browser Closed");
    }

    public static WebDriver driver() {
        return driver;
    }

    public static void get(String url) {
        driver().get(url);
    }

    public static WebDriverWait getWebDriverWait(long timeout) {
        return new WebDriverWait(driver(), timeout);
    }

}
