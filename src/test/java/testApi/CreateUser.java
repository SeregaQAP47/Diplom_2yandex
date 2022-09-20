package testApi;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUser {
    private Response response;
    private String emailUser = "userJo@ya.ru";
    private String nameUser = "John";
    private String passwordUser = "202098";
    private String token;


    @Test
    @Tag("positive")
    @DisplayName("Тестирование создания нового пользователя")
    @Description("Тестирование метода POST /auth/register" +
            "<p> Метод создает нового пользователя </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 200 </li>" +
            "<li> email в теле ответа совпадает с email в запросе </li>" +
            "<li> name в теле ответа совпадает с name в запросе </li>" +
            "<li> success в теле ответа = true </li>" +
            "<li> accessToken в теле ответа не пустой </li>" +
            "<li> refreshToken в теле ответа не пустой </li>")
    public void testCreateUniqueUser() {
        User user = new User(emailUser, passwordUser, nameUser);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            checkBodyResponse(emailUser, nameUser);
            token = getToken(response);
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Создание пользователя который уже существует")
    @Description("Тестирование метода POST /auth/register" +
            "<p> Создание уже существующего пользователя </p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 403 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"User already exists\" </li>")
    public void testCreateDuplicateUser() {
        RequestCustom requestCustom = new RequestCustom();
        User user = new User("greatMan@mail.ru", "qqer231", "Sam");
        step(("Отправить запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        step(("Отправить дублирующий запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_FORBIDDEN);
            checkResponseErrorMessage(false, "User already exists");
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Создание пользователя без обязательного поля \"password\"")
    @Description("Тестирование метода POST /auth/register" +
            "<p> Создание пользователя без обязательного поля password</p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 403 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"Email, password and name are required fields\" </li>")
    public void testCreateUserWithoutRequiredField_Password() {
        RequestCustom requestCustom = new RequestCustom();
        User user = new User();
        user.setEmail("testUser1@ya.ru");
        user.setName("Yan");
        step(("Отправить запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_FORBIDDEN);
            checkResponseErrorMessage(false, "Email, password and name are required fields");
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Создание пользователя без обязательного поля \"name\"")
    @Description("Тестирование метода POST /auth/register" +
            "<p> Создание пользователя без обязательного поля name</p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 403 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"Email, password and name are required fields\" </li>")
    public void testCreateUserWithoutRequiredField_Name() {
        RequestCustom requestCustom = new RequestCustom();
        User user = new User();
        user.setEmail("testUser1@ya.ru");
        user.setPassword("123quy");
        step(("Отправить запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_FORBIDDEN);
            checkResponseErrorMessage(false, "Email, password and name are required fields");
        });
    }

    @Test
    @Tag("negative")
    @DisplayName("Создание пользователя без обязательного поля \"email\"")
    @Description("Тестирование метода POST /auth/register" +
            "<p> Создание пользователя без обязательного поля email</p>" +
            "<ul> Проверяем: </ul>" +
            "<li> Статус код 403 </li>" +
            "<li> JSON c корректной структурой в теле ответа </li>" +
            "<li> Поле success в теле ответа = false </li>" +
            "<li> Message в теле ответа - \"Email, password and name are required fields\" </li>")
    public void testCreateUserWithoutRequiredField_Email() {
        RequestCustom requestCustom = new RequestCustom();
        User user = new User();
        user.setPassword("qw123gh");
        user.setName("Yan");
        step(("Отправить запрос POST /auth/register"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_FORBIDDEN);
            checkResponseErrorMessage(false, "Email, password and name are required fields");
        });
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
