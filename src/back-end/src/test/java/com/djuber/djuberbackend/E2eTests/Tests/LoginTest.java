package com.djuber.djuberbackend.E2eTests.Tests;

import com.djuber.djuberbackend.E2eTests.Pages.HomePage;
import com.djuber.djuberbackend.E2eTests.Pages.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {

    public static WebDriver driver;

    private final String TITLE = "DJUBER";

    private final String LOGGED_NAME = "Andrej Culjak";

    private final String INVALID_EMAIL_ERROR = "Invalid email";

    private final String LOGIN_PAGE_URL = "http://localhost:4200/authentication/login";


    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/draga/OneDrive/Dokumenti/4. Godina/KTS/chromedriver.exe");
        driver = new ChromeDriver();
        //maximize the window
        driver.manage().window().maximize();
    }

    @AfterClass
    public void quitDriver() {
        driver.quit();
    }

    @Test
    public void loginTest() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        Assertions.assertEquals(TITLE, homePage.getTitleContent());

        homePage.clickLoginButton();

        loginPage.enterEmailInvalid();
        loginPage.login();
        loginPage.clearEmail();


        loginPage.enterEmailInvalid();
        loginPage.login();
        Assertions.assertEquals(INVALID_EMAIL_ERROR, loginPage.getErrorText());
        loginPage.clearEmail();

        loginPage.enterWrongEmail();
        loginPage.enterPasswordValid();
        loginPage.login();

        Assertions.assertEquals(LOGIN_PAGE_URL, driver.getCurrentUrl());

        loginPage.clearEmail();
        loginPage.enterEmailValid();
        loginPage.login();

        homePage.clickMenuButton();
        Assertions.assertEquals(LOGGED_NAME, homePage.getLoggedUserName());
    }
}
