package com.djuber.djuberbackend.E2eTests.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    private Actions action;

    @FindBy(id = "emailLoginInput")
    WebElement emailInput;

    @FindBy(id = "passwordLoginInput")
    WebElement passwordInput;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(id = "emailError")
    WebElement emailError;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.action = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterEmailValid(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(emailInput)).sendKeys("asi@maildrop.cc");
    }

    public void enterEmailInvalid(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(emailInput)).sendKeys("asi");
    }

    public void enterWrongEmail(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(emailInput)).sendKeys("asi@gg.com");
    }

    public String getErrorText(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.textToBePresentInElement(emailError,"Invalid email"));
        return emailError.getText();
    }

    public void clearEmail(){
        emailInput.clear();
    }

    public void enterPasswordValid(){
        passwordInput.sendKeys("asi123");
    }

    public void login(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
}
