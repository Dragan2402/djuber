package com.djuber.djuberbackend.E2eTests.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;

    private Actions action;

    @FindBy(id = "title")
    WebElement title;

    @FindBy(id = "login-div")
    WebElement loginDiv;

    @FindBy(id = "menuButton")
    WebElement menuButton;

    @FindBy(id = "userName")
    WebElement loggedUserName;

    @FindBy(id = "orderRadio")
    WebElement orderRadioButton;

    @FindBy(id = "differentRide")
    WebElement differentRideCheckBox;

    @FindBy(id = "startAddressInput")
    WebElement startAddressInput;

    @FindBy(id = "desiredAddressInput")
    WebElement desiredAddressInput;

    @FindBy(id = "createRouteButton")
    WebElement createRouteButton;

    @FindBy(id = "orderRideButton")
    WebElement orderRideButton;

    @FindBy(id = "showOnMapButton")
    WebElement showOnMapButton;

    private static String PAGE_URL="http://localhost:4200/homePage";

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.action = new Actions(driver);
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public String getTitleContent(){
        return (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(title)).getText();
    }

    public void clickLoginButton(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(loginDiv)).click();
    }

    public void clickMenuButton(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(menuButton)).click();
    }

    public String getLoggedUserName(){
        return (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(loggedUserName)).getText();
    }

    public void clickOrderRadioButton(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(orderRadioButton)).click();
    }

    public void enterStartingAddress(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(startAddressInput)).sendKeys("Hopovska 4 Novi Sad");
    }

    public void enterDesiredAddress(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(desiredAddressInput)).sendKeys("Balans palacinke");
    }

    public void clickDifferentRideCheckBox(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(differentRideCheckBox)).click();
    }

    public void clickCreateRideButton(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(createRouteButton)).click();
    }

    public void clickOrderRideButton(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(orderRideButton)).click();
    }

    public void clickShowOnMapButton(){
        (new WebDriverWait(driver, Duration.ofMinutes(3))).until(ExpectedConditions.elementToBeClickable(showOnMapButton)).click();
    }
}
