package testPage.ByYandex;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.*;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;

public class TestLogin_Yandex {
    private WebDriver driver;
    private static Response response;
    private static String email = "jackTest@test.ru";
    private static String name = "Jack";
    private static String password = "8709test";
    private static String token;
    private static RequestCustom requestCustom = new RequestCustom();
    private String URL = "https://stellarburgers.nomoreparties.site/";

    @Before
    //Инициализация Yandex driver
    public void startYandex() {
        System.setProperty("webdriver.chrome.driver", "C:/yandexdriver.exe");
    }

    @BeforeClass
    public static void registrationUser() {
        User user = new User(email, password, name);
        response = requestCustom.postCreateUser(user);
        token = response.then().extract().body().jsonPath().getString("accessToken");
        System.out.println("Create user");
    }

    @Test
    @DisplayName("Вход через кнопку \"Войти в аккаунт\".")
    @Description("Тестирование входа через кнопку \"Войти в аккаунт\" на главное странице." +
            "<ul> Проверяем: </ul>" +
            "<li> Вход успешно выполнен </li>" +
            "<li> На главной странице отобразится кнопка \"Оформить заказ\" </li>")
    public void testSingInThroughTheLoginToAccountButton() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox");
        driver = new ChromeDriver(options);
        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку \"Войти в аккаунт\""), () -> {
            mainPage.clickButtonEnterAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Заполнить поля email и пароль и нажать кнопку войти"), () -> {
            loginPage.waitLoadPageLogin();
            loginPage.authUser(email, password);
        });
        step(("Проверяем"), () -> {
            String placeAnOrder = mainPage.getTextPlaceAnOrder();
            assertEquals("Оформить заказ", placeAnOrder);
        });
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        step(("Выйти из аккаунта"), () -> {
            mainPage.clickButtonPersonalAccount();
            personalAccountPage.clickExit();
        });
    }

    @Test
    @DisplayName("Вход через кнопку \"Личный кабинет\".")
    @Description("Тестирование входа через кнопку \"Личный кабинет\" на главное странице." +
            "<ul> Проверяем: </ul>" +
            "<li> Вход успешно выполнен </li>" +
            "<li> На главной странице отобразится кнопка \"Оформить заказ\" </li>")
    public void testSingInThroughButtonPersonalAccount() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox");
        driver = new ChromeDriver(options);
        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку \"Личный кабинет\""), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Заполнить поля email и пароль и нажать кнопку войти"), () -> {
            loginPage.waitLoadPageLogin();
            loginPage.authUser(email, password);
        });
        step(("Проверяем"), () -> {
            String placeAnOrder = mainPage.getTextPlaceAnOrder();
            assertEquals("Оформить заказ", placeAnOrder);
        });
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        step(("Выйти из аккаунта"), () -> {
            mainPage.clickButtonPersonalAccount();
            personalAccountPage.clickExit();
        });
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации.")
    @Description("Тестирование входа через кнопку в форме регистрации." +
            "<ul> Проверяем: </ul>" +
            "<li> Вход успешно выполнен </li>" +
            "<li> На главной странице отобразится кнопка \"Оформить заказ\" </li>")
    public void testSingInThroughRegisterPage() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox");
        driver = new ChromeDriver(options);
        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажимаем на кнопку \"Личный кабинет\""), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Откроется станица входа"), () -> {
            loginPage.waitLoadPageLogin();
            loginPage.scrollToRegisterAndClick();
        });
        RegisterPage registerPage = new RegisterPage(driver);
        step(("Откроется страница регистрации пользователя"), () -> {
            registerPage.waitLoadRegisterPage();
            registerPage.clickButtonEnter();
        });
        step(("Заполнить поля email и пароль и нажать кнопку войти"), () -> {
            loginPage.waitLoadPageLogin();
            loginPage.authUser(email, password);
        });
        step(("Проверяем"), () -> {
            String placeAnOrder = mainPage.getTextPlaceAnOrder();
            assertEquals("Оформить заказ", placeAnOrder);
        });
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        step(("Выйти из аккаунта"), () -> {
            mainPage.clickButtonPersonalAccount();
            personalAccountPage.clickExit();
        });
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля.")
    @Description("Тестирование входа через кнопку в форме восстановления пароля." +
            "<ul> Проверяем: </ul>" +
            "<li> Вход успешно выполнен </li>" +
            "<li> На главной странице отобразится кнопка \"Оформить заказ\" </li>")
    public void testSingInThroughPasswordRecovery() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox");
        driver = new ChromeDriver(options);
        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажимаем на кнопку \"Личный кабинет\""), () -> {
            mainPage.clickButtonPersonalAccount();
        });
        LoginPage loginPage = new LoginPage(driver);
        step(("Откроется станица входа"), () -> {
            loginPage.waitLoadPageLogin();
        });
        step(("Нажать кнопку \"Восстановить пароль\""), () -> {
            loginPage.clickButtonRestorationPassword();
        });
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        step(("Откроется страница восстановления пароля пользователя"), () -> {
            forgotPasswordPage.waitTitlePasswordRecovery();
        });
        step(("Нажать кнопку \"Войти\""), () -> {
            forgotPasswordPage.clickButtonEnter();
        });
        step(("Откроется станица входа"), () -> {
            loginPage.waitLoadPageLogin();
        });
        step(("Заполнить поля email и пароль и нажать кнопку войти"), () -> {
            loginPage.waitLoadPageLogin();
            loginPage.authUser(email, password);
        });
        step(("Проверяем"), () -> {
            String placeAnOrder = mainPage.getTextPlaceAnOrder();
            assertEquals("Оформить заказ", placeAnOrder);
        });
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        step(("Выйти из аккаунта"), () -> {
            mainPage.clickButtonPersonalAccount();
            personalAccountPage.clickExit();
        });
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @AfterClass
    public static void deleteUser() {
        response = requestCustom.deleteUserTokenHeader(token);
        response.then().statusCode(202);
        System.out.println("Delete OK");
    }
}
