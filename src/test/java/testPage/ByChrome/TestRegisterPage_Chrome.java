package testPage.ByChrome;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.LoginPage;
import pageObjects.MainPage;
import pageObjects.RegisterPage;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;


public class TestRegisterPage_Chrome {
    private WebDriver driver;
    private Response response;
    private final String URL = "https://stellarburgers.nomoreparties.site/";
    private String userName = "Un";
    private String userEmail = "222@mail.ru";
    private String userPassword = "we23sd34";

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver105.exe");
    }

    @Test
    @DisplayName("Регистрация пользователя с валидными параметрами.")
    @Description("Тестирование регистрации пользователя с валидными параметрами." +
            "<ul> Проверяем: </ul>" +
            "<li> Регистрация успешно выполнена </li>" +
            "<li> Отобразилась страница для входа в приложение </li>")
    public void testValidRegistration() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--no-sandbox"); //"--start-maximized" - во весь экран
        driver = new ChromeDriver(options);

        step(("Открываем главную страницу"), () -> {
            driver.get(URL);
        });
        MainPage mainPage = new MainPage(driver);
        step(("Нажимаем на кнопку \"Личный кабинет\""), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Откроется станица входа"), () -> {
            loginPage.waitLoadPageLogin();
        });
        step(("Делаем скрол до кнопки \"Зарегистрироваться\" и нажимаем на неё"), () -> {
            loginPage.scrollToRegisterAndClick();
        });
        RegisterPage registerPage = new RegisterPage(driver);
        step(("Откроется страница регистрации пользователя"), () -> {});
            registerPage.waitLoadRegisterPage();
            registerPage.registerUser(userName, userEmail, userPassword);

        step(("Проверяем что отобразилась страница входа"), () -> {
            assertEquals("Вход", loginPage.getTextTitle());
        });
        deleteUser();
    }

    @Test
    @DisplayName("Регистрация пользователя с не корректным паролем.")
    @Description("Тестирование регистрации пользователя с не корректным паролем параметрами." +
            "<p> Пароль должен быть не менее 6 символов </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Регистрация не выполнена </li>" +
            "<li> Сообщение о не корректном пароле </li>")
    public void testNotCorrectPassword() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--no-sandbox"); //"--start-maximized" - во весь экран
        driver = new ChromeDriver(options);

        step(("Открываем главную страницу"), () -> {
            driver.get(URL);
        });
        MainPage mainPage = new MainPage(driver);
        step(("Нажимаем на кнопку \"Личный кабинет\""), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Откроется станица входа"), () -> {
            loginPage.waitLoadPageLogin();
        });
        step(("Делаем скрол до кнопки \"Зарегистрироваться\" и нажимаем на неё"), () -> {
            loginPage.scrollToRegisterAndClick();
        });
        RegisterPage registerPage = new RegisterPage(driver);
        step(("Откроется страница регистрации пользователя"), () -> {
            registerPage.waitLoadRegisterPage();
        });
        step(("Вводим в поле Имя - имя пользователя"), () -> {
            registerPage.setName("Ra");
        });
        step(("Вводим в поле Email - email пользователя"), () -> {
            registerPage.setEmail("raRa@try.com");
        });
        step(("Вводим в поле Пароль - не корректный пароль из 5 символов"), () -> {
            registerPage.setPassword("Ra");
        });
        step(("Нажать кнопку \"Зарегистрироваться\""), () -> {
            registerPage.buttonRegisterClick();
        });
        step(("Проверяем что отобразилась сообщение о некорректном пароле"), () -> {
            assertEquals("Некорректный пароль", registerPage.getErrorMessageNotCorrectPassword());
        });
    }




    private void deleteUser() {
        RequestCustom requestCustom = new RequestCustom();
        User user = new User(userEmail, userPassword, userName);
        response = requestCustom.postLoginUser(user);
        String accessToken = response.then().extract().body().jsonPath().getString("accessToken");
        response = requestCustom.deleteUserTokenHeader(accessToken);
        response.then().statusCode(202);
    }


    @After
    public void teardown() {
        driver.quit();
    }
}
