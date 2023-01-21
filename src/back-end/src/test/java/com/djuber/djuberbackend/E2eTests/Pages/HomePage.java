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
}
