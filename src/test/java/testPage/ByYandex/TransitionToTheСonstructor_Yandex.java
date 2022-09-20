package testPage.ByYandex;

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

//Переход из личного кабинета в конструктор
public class TransitionToTheСonstructor_Yandex {
    private WebDriver driver;
    private String URL = "https://stellarburgers.nomoreparties.site/";


    @Before
    //Инициализация Yandex driver
    public void startYandex() {
        System.setProperty("webdriver.chrome.driver", "C:/yandexdriver.exe");
    }

    @Test
    @DisplayName("Переход в конструктор из личного кабинета, через кнопку \"Конструктор\". Chrome")
    @Description("Тестирование перехода в конструктор из личного кабинета" +
            "<p> Для тестирование используется браузер Yandex</p>" +
            "<p> Проверяем: </p>" +
            "<p> Переход в конструктор выполнен успешно</p>")
    public void testTransitionConstructorThroughTheConstructorButton() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //"--start-maximized" - для запуска в максимальном размере
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
        step(("Нажать на кнопку \"Конструктор\""), () -> {
            loginPage.clickButtonConstructor();
        });
        step(("Откроется главная страница"), () -> {
            mainPage.waitLoadPageMain();
        });
        step(("Проверяем"), () -> {
            assertEquals("Соберите бургер", mainPage.getTextHeading());
        });
    }

    @Test
    @DisplayName("Переход в конструктор из личного кабинета, через логотип \"Stellar Burgers\"")
    @Description("Тестирование перехода в конструктор через логотип \"Stellar Burgers\"" +
            "<p> Для тестирование используется браузер Yandex</p>" +
            "<p> Проверяем: </p>" +
            "<p> Переход в конструктор выполнен успешно</p>")
    public void testTransitionConstructorThroughLogo_StellarBurgers() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //"--start-maximized" - для запуска в максимальном размере
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
        step(("Нажать на логотип \"Stellar Burgers\" в верхней части страницы"), () -> {
            loginPage.clickLogoStellarBurgers();
        });
        step(("Откроется главная страница"), () -> {
            mainPage.waitLoadPageMain();
        });
        step(("Проверяем"), () -> {
            assertEquals("Соберите бургер", mainPage.getTextHeading());
        });
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
