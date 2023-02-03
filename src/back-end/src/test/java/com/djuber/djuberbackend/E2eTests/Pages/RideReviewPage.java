package com.djuber.djuberbackend.E2eTests.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RideReviewPage {

    private WebDriver driver;

    private Actions action;

    @FindBy(id = "carRatingOption")
    WebElement carRatingOption;

    @FindBy(id = "driverRatingOption")
    WebElement driverRatingOption;

    @FindBy(id = "commentInput")
    WebElement commentInput;

    @FindBy(id = "sumbitReviewButton")
    WebElement sumbitReviewButton;

    public RideReviewPage(WebDriver driver) {
        this.driver = driver;
        this.action = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterComment(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(commentInput)).sendKeys("Sve pohvale.");
    }
    public void clickCarRatingOption(){
        (new WebDriverWait(driver, Duration.ofMinutes(5))).until(ExpectedConditions.elementToBeClickable(carRatingOption)).click();
    }
    public void clickDriverRatingOption(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(driverRatingOption)).click();
    }
    public void clickSubmitReview(){
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.elementToBeClickable(sumbitReviewButton)).click();
    }
}