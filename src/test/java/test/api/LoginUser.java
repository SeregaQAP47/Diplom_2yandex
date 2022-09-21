package test.api;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUser {
    private Response response;
    private String token;
    String userEmail = "testUserlog@ya.ru";
    String userPassword = "12kjhj";
    String userName = "Erik";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    @Test
    @Tag("positive")
    @DisplayName("Регистрация пользователя")
    @Description("Тестирование запроса POST /auth/login" +
            "<p> Регистрация пользователя с валидными email и password</p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 200 </li>" +
            "<li> email в теле ответа совпадает с email в запросе </li>" +
            "<li> name в теле ответа совпадает с name в запросе </li>" +
            "<li> success в теле ответа = true </li>" +
            "<li> accessToken в теле ответа не пустой </li>" +
            "<li> refreshToken в теле ответа не пустой </li>")
    public void testLoginUser() {
        User user = new User(userEmail, userPassword, userName);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос POST /auth/register для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        step(("Отправить запрос POST /auth/login для регистрация пользователя"), () -> {
            response = requestCustom.postLoginUser(user);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_OK);
            checkBodyResponse(userEmail, userName);
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Регистрация пользователя с не верным паролем")
    @Description("Тестирование метода POST /auth/login" +
            "<p> Регистрация пользователя с не верным паролем </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 401 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"email or password are incorrect\" </li>")
    public void testLogin() {
        String email = "testLUser@mail.ru";
        String password = "pjuy45";
        String name = "Lars";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос POST /auth/register для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        User user2 = new User(email, "1234", name);

        step(("Отправить запрос POST /auth/login для регистрация пользователя"), () -> {
            response = requestCustom.postLoginUser(user2);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
            checkResponseErrorMessage(false, "email or password are incorrect");
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Регистрация пользователя с не верным email")
    @Description("Тестирование метода POST /auth/login" +
            "<p> Регистрация пользователя с не верным email </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 401 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"email or password are incorrect\" </li>")
    public void testLoginEmail() {
        String email = "UserGreen@mail.ru";
        String password = "pjuy45";
        String name = "Mr.Green";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос POST /auth/register для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        User user2 = new User("Green@ya.ru", password, name);

        step(("Отправить запрос POST /auth/login для регистрация пользователя"), () -> {
            response = requestCustom.postLoginUser(user2);
        });
        step(("Получили ответ"), () -> {
            response.prettyPeek();
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
            checkResponseErrorMessage(false, "email or password are incorrect");
        });
    }

    @Step("Проверка тела ответа")
    private void checkBodyResponse(String expectEmail, String expectName) {
        System.out.println("Начало проверки");
        response.then().assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(expectEmail.toLowerCase()))
                .and().assertThat().body("user.name", equalTo(expectName))
                .and().assertThat().body("accessToken", notNullValue())
                .and().assertThat().body("refreshToken", notNullValue());
        System.out.println("Проверка завершена");
    }

    @Step("Проверяем, что статус код = {httpStatus}")
    private void checkStatus(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }

    @Step("Получение токена")
    private String getToken(Response response) {
        String accessToken = response.then().extract().body().jsonPath().getString("accessToken");
        return accessToken.substring(7);
    }

    @After
    @Step("Удаление пользователя")
    public void userDelete() {
        if (token != null) {
            RequestCustom requestCustom = new RequestCustom();
            requestCustom.deleteUser(token);
        }
    }

    @Step
    private void checkResponseErrorMessage(boolean expectSuccess, String expectMessage) {
        response.then().assertThat().body("success", equalTo(expectSuccess))
                .and().assertThat().body("message", equalTo(expectMessage));
    }
}
