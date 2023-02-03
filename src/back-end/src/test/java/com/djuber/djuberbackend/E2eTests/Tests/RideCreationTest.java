package com.djuber.djuberbackend.E2eTests.Tests;

import com.djuber.djuberbackend.E2eTests.Pages.HomePage;
import com.djuber.djuberbackend.E2eTests.Pages.LoginPage;
import com.djuber.djuberbackend.E2eTests.Pages.RideReviewPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RideCreationTest {

    public static WebDriver driver;

    public static WebDriver driverDriver;

    private final String TITLE = "DJUBER";

    private final String HOME_PAGE_URL = "http://localhost:4200/homePage";

    private final String SINGLE_RIDE_URL = "http://localhost:4200/singleRideMap/1";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/draga/OneDrive/Dokumenti/4. Godina/KTS/chromedriver.exe");
        driver = new ChromeDriver();
        //maximize the window
        driver.manage().window().maximize();

        driverDriver = new ChromeDriver();
        driverDriver.manage().window().maximize();
    }

    @AfterClass
    public void quitDriver() {
        driver.quit();
        driverDriver.quit();
    }

    @Test
    public void rideCreationTest() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        RideReviewPage rideReviewPage = new RideReviewPage(driver);

        HomePage homePageDriver = new HomePage(driverDriver);
        LoginPage loginPageDriver = new LoginPage(driverDriver);

        homePageDriver.clickLoginButton();
        loginPageDriver.enterDriverMail();
        loginPageDriver.enterDriverPassword();
        loginPageDriver.login();
        Assertions.assertEquals(TITLE, homePage.getTitleContent());

        homePage.clickLoginButton();

        loginPage.enterEmailValid();
        loginPage.enterPasswordValid();
        loginPage.login();

        homePage.clickOrderRadioButton();
        homePage.clickDifferentRideCheckBox();
        homePage.enterStartingAddress();
        homePage.enterDesiredAddress();
        homePage.clickCreateRideButton();
        homePage.clickOrderRideButton();
        homePage.clickShowOnMapButton();
        homePageDriver.clickShowOnMapButton();
        Assertions.assertEquals(SINGLE_RIDE_URL, driver.getCurrentUrl());
        Assertions.assertEquals(SINGLE_RIDE_URL, driverDriver.getCurrentUrl());

        rideReviewPage.clickCarRatingOption();
        rideReviewPage.clickDriverRatingOption();
        rideReviewPage.enterComment();
        rideReviewPage.clickSubmitReview();

        Assertions.assertEquals(TITLE, homePage.getTitleContent());
        Assertions.assertEquals(HOME_PAGE_URL, driver.getCurrentUrl());
        Assertions.assertEquals(HOME_PAGE_URL, driverDriver.getCurrentUrl());
    }
}
