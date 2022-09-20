package testApi;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import pojo.User;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RefactorUser {
    private Response response;
    private String token;


    @Test
    @DisplayName("Изменение имени  не авторизованного пользователя")
    @Description("Тестирование метода PATCH /auth/user" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 401 </p>")
    public void testNameUserIs_Not_LoggedIn() {
        String email = "userJack@ya.ru";
        String name = "Jack";
        String password = "8709ty";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        String nameModified = "Jackson";
        User userName = new User();
        userName.setName(nameModified);
        step(("Отправить запрос для обновления данных"), () -> {
            response = requestCustom.patchUser(token, userName);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
        });
    }

    @Test
    @DisplayName("Изменение email не авторизованного пользователя")
    @Description("Тестирование метода PATCH /auth/user" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 401 </p>")
    public void testRefreshEmailUserIs_Not_LoggedIn() {
        String email = "userMor@ya.ru";
        String name = "Morty";
        String password = "8709ty";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        String emailModified = "userMorty@mail.ru";
        User userEmail = new User();
        userEmail.setEmail(emailModified);
        step(("Отправить запрос для обновления данных"), () -> {
            response = requestCustom.patchUser(token, userEmail);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
        });
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    @Description("Тестирование метода PATCH /auth/user" +
            "<p> Изменение email у авторизованного пользователя </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 200 </p>" +
            "<p> Email изменен </p>")
    public void testRefreshEmailUserIsLoggedIn() {
        String email = "userDryMor@ya.ru";
        String name = "Morty";
        String password = "8709ty";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Регистрация пользователя"), () -> {
            response = requestCustom.postLoginUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        String emailModified = "userMorty@mail.ru";
        User userEmail = new User();
        userEmail.setEmail(emailModified);
        step(("Отправить запрос для обновления данных"), () -> {
            response = requestCustom.patchUser(token, userEmail);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            checkResponse(emailModified, name);
        });
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    @Description("Тестирование метода PATCH /auth/user" +
            "<p> Изменение имени у авторизованного пользователя </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 200 </p>" +
            "<p> Email изменен </p>")
    public void testRefreshNameUserIsLoggedIn() {
        String email = "userJacky@ya.ru";
        String name = "Chen";
        String password = "8709ty";
        User user = new User(email, password, name);
        RequestCustom requestCustom = new RequestCustom();
        step(("Отправить запрос для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Регистрация пользователя"), () -> {
            response = requestCustom.postLoginUser(user);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            token = getToken(response);
        });
        String nameModified = "Jackie";
        User userName = new User();
        userName.setName(nameModified);
        step(("Отправить запрос для обновления данных"), () -> {
            response = requestCustom.patchUser(token, userName);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            checkResponse(email, nameModified);
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

    @After
    @Step("Удаление пользователя")
    public void userDelete() {
        if (token != null) {
            RequestCustom requestCustom = new RequestCustom();
            requestCustom.deleteUser(token);
        }
    }

    @Step("Проверка полученного ответа")
    private void checkResponse(String expectEmail, String expectName) {
        response.then().assertThat().body("success", equalTo(true))
                .and().assertThat().body("user.email", equalTo(expectEmail.toLowerCase()))
                .and().assertThat().body("user.name", equalTo(expectName));
    }
}
