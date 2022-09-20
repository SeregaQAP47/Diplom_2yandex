package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Страница "Личный кабинет"
 */
public class PersonalAccountPage {
    private WebDriver driver;

    public PersonalAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    //Кнопка "Выход"
    private By buttonExit = By.xpath("//button[@type = \"button\" and text()=\"Выход\"]");
    //Кнопка "Конструктор"
    private By buttonConstructor = By.xpath("//p[text()=\"Конструктор\"]");

    public void clickExit() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(buttonExit));
        driver.findElement(buttonExit).click();
    }

    public String getTextButtonExit() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(buttonExit));
        return driver.findElement(buttonExit).getText();
    }
}
