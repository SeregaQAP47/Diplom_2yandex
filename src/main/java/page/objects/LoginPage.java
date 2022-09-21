package page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Страница входа
 */
public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    //Поле ввода Email
    private By emailInput = By.cssSelector(".text.input__textfield.text_type_main-default[name=\"name\"]");
    //Поле ввода Password
    private By passwordInput = By.cssSelector(".text.input__textfield.text_type_main-default[type=\"password\"]");
    //Кнопка "Войти"
    private By buttonAuthEnter = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_medium__3zxIa");
    //Переход на страницу регистрации
    private By linkPageRegister = By.cssSelector(".Auth_link__1fOlj[href=\"/register\"]");
    //Заголовок "Вход"
    private By h2Enter = By.xpath("//h2[text() = \"Вход\"]");
    //Кнопка "Восстановить пароль"
    private By buttonRestorationPassword = By.cssSelector(".Auth_link__1fOlj[href=\"/forgot-password\"]");
    //Кнопка "Конструктор"
    private By buttonConstructor = By.xpath("//p[text()=\"Конструктор\"]");
    //Логотип Stellar Burgers
    private By logoStellarBurgers = By.xpath("//div/a[@href=\"/\"]");

    //Нажатие кнопки конструктор
    public void clickButtonConstructor() {
        driver.findElement(buttonConstructor).click();
    }

    //Нажатие на логотип Stellar Burgers
    public void clickLogoStellarBurgers() {
        driver.findElement(logoStellarBurgers).click();
    }

    public void clickButtonRestorationPassword() {
        WebElement element = driver.findElement(buttonRestorationPassword);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void scrollToRegisterAndClick() {
        WebElement element = driver.findElement(linkPageRegister);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void waitLoadPageLogin() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(h2Enter));
    }

    public String getTextTitle() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(h2Enter));
        return driver.findElement(h2Enter).getText();
    }

    /**
     * Метод осуществляет вход в приложение
     *
     * @param email    -
     * @param password
     */
    public void authUser(String email, String password) {
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(buttonAuthEnter).click();
    }
}
