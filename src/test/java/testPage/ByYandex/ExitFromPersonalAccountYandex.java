package testPage.ByYandex;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.LoginPage;
import pageObjects.MainPage;
import pageObjects.PersonalAccountPage;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;

public class ExitFromPersonalAccountYandex {
    private WebDriver driver;
    private static Response response;
    private static String email = "QQQTest@test.ru";
    private static String name = "Jack";
    private static String password = "45678test";
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
    @DisplayName("Выход из личного кабинета")
    @Description("Выход из личного кабинета." +
            "<p> Браузер для тестирования Yandex. </p>" +
            "<p> Проверяем: </p>" +
            "<p> Успешный выход из личного кабинета </p>")
    public void testExitFromPersonalAccount() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--no-sandbox");
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
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        step(("Выйти из аккаунта"), () -> {
            mainPage.clickButtonPersonalAccount();
            personalAccountPage.clickExit();
        });
        step(("Проверяем"), () -> {
            String enterLogo = loginPage.getTextTitle();
            assertEquals("Вход", enterLogo);
        });

    }

    @AfterClass
    public static void deleteUser() {
        response = requestCustom.deleteUserTokenHeader(token);
        response.then().statusCode(202);
        System.out.println("Delete OK");
    }

    @After
    public void teardown() {
        driver.quit();
    }
}
