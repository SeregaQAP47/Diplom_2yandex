package pageObjects;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Страница регистрации
 */
public class RegisterPage {
    private WebDriver driver;

//    https://stellarburgers.nomoreparties.site/register


    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    //Поле ввода "Имя" и поле "Email"
    private By nameAndEmailInput = By.cssSelector(".text.input__textfield.text_type_main-default[name=\"name\"]");
    //Поле ввода "Password"
    private By passwordInput = By.cssSelector(".text.input__textfield.text_type_main-default[type=\"password\"]");
    //Кнопка "Зарегистрироваться"
    private By buttonRegister = By.xpath("//button[text() =\"Зарегистрироваться\"]");
    //Заголовок "Регистрация"
    private By h2Register = By.xpath("//h2[text()=\"Регистрация\"]");
    //Сообщение о не корректном пароле
    private By errorPassword = By.cssSelector(".input__error.text_type_main-default");
    //Кнопка "Войти"
    private By buttonEnter = By.cssSelector(".Auth_link__1fOlj");



    public void waitLoadRegisterPage() {
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(h2Register));
    }

    public void clickButtonEnter() {
        WebElement element = driver.findElement(buttonEnter);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }



    //Метод ожидает сообщение о вводе не корректного пароля и возвращает строку с текстом ошибки
    public String getErrorMessageNotCorrectPassword() {
       return new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.visibilityOfElementLocated(errorPassword)).getText();
    }


    //Метод заполняет поле "Имя"
    public void setName(String userName) {
        List<WebElement> webElements = driver.findElements(nameAndEmailInput);
        webElements.get(0).sendKeys(userName);
    }

    //Метод заполняет поле "Eamil"
    public void setEmail(String userEmail) {
        List<WebElement> webElements = driver.findElements(nameAndEmailInput);
        webElements.get(1).sendKeys(userEmail);
    }

    //Метод заполняет поле "Password"
    public void setPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    //Метод нажимает кнопку "Зарегистрироваться"
    public void buttonRegisterClick() {
        driver.findElement(buttonRegister).click();
    }
    /**
     * Метод выполняет регистрацию пользователя
     *
     * @param userName
     * @param userEmail
     * @param password
     */

    public void registerUser(String userName, String userEmail, String password) {
        List<WebElement> webElements = driver.findElements(nameAndEmailInput);
        webElements.get(0).sendKeys(userName);
        webElements.get(1).sendKeys(userEmail);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(buttonRegister).click();
    }

}
