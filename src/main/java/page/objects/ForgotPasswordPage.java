package page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Страница восстановления пароля
 */
public class ForgotPasswordPage {
    private WebDriver driver;

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    //Заголовок "Восстановление пароля"
    private By titlePasswordRecovery = By.xpath("//h2[text()=\"Восстановление пароля\"]");
    //Кнопка "Войти"
    private By buttonEnter = By.cssSelector(".Auth_link__1fOlj");

    public void waitTitlePasswordRecovery() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(titlePasswordRecovery));
    }

    public void clickButtonEnter() {
        WebElement element = driver.findElement(buttonEnter);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }
}
