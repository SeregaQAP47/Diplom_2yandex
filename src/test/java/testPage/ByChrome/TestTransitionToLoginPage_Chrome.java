package testPage.ByChrome;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.LoginPage;
import pageObjects.MainPage;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;

public class TestTransitionToLoginPage_Chrome {
    private WebDriver driver;
    private String URL = "https://stellarburgers.nomoreparties.site/";

    //Инициализация Chrome driver
    @Before
    public void startChrome() {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver105.exe");
    }

    @Test
    @DisplayName("Переход в \"Личный кабинет\" Chrome.")
    @Description("Тестирование в \"Личный кабинет\" с главной страницы." +
            "<p> Тестирование выполняется в браузере Chrome </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Переход в личный кабинет успешно выполнен </li>")
    public void testTransferPersonalAccount_Chrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //для запуска в полноэкранном режиме "--start-maximized"
        driver = new ChromeDriver(options);
        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку Личный кабинет"), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Откроется страница Личный кабинет"), () -> {
            loginPage.waitLoadPageLogin();
        });
        step(("Проверяем"), () -> {
            assertEquals("Вход", loginPage.getTextTitle());
        });
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
