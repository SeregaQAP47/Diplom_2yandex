package test.api;

import helper.RequestCustom;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import pojo.Ingredient;
import pojo.Order;
import pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;

public class GetOrder {
    private Response response;
    private RequestCustom requestCustom = new RequestCustom();
    private String emailUser = "Tset@mail.ru";
    private String passwordUser = "tefrt023";
    private String nameUser = "Bee";
    private String token;
    private String tokenLogin;


    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    @Description("Тестирование запроса GET /orders" +
            "<p> Запрос получает информацию о заказах авторизованного пользователя используя token </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 200 </p>" +
            "<p> JSON с корректной структурой в теле ответа </p>")
    public void testGetOrderUserAuthorization() {
        User user = new User(emailUser, passwordUser, nameUser);
        step(("Отправить запрос POST /auth/reqister для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
            token = getToken(response);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Отправить запрос Запрос POST /auth/login для авторизации пользователя"), () -> {
            response = requestCustom.postLoginUser(user);
            tokenLogin = getToken(response);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        Order order = new Order();
        List<String> listIngredients = new ArrayList<>();
        step(("Генерируем тело запроса"), () -> {
            listIngredients.add(getRandomIngredient());
            listIngredients.add(getRandomIngredient());
            order.setIngredients(listIngredients);
        });
        step(("Отправить запрос POST /orders для создания заказа"), () -> {
            response = requestCustom.postOrders(order, tokenLogin);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Отправить запрос GET /orders для получения информации о заказе"), () -> {
            response = requestCustom.getOrders(token);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            checkResponseOrderUserAuthorization();
        });

    }

    @Test
    @DisplayName("Получение заказа не авторизованного пользователя")
    @Description("Тестирование запроса GET /orders" +
            "<p> Запрос получает информацию о заказах авторизованного пользователя используя token </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 401 </p>" +
            "<p> JSON с корректной структурой в теле ответа </p>")
    public void testGetOrderUser_NOT_Authorization() {
        User user = new User(emailUser, passwordUser, nameUser);
        step(("Отправить запрос POST /auth/reqister для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
            token = getToken(response);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        Order order = new Order();
        List<String> listIngredients = new ArrayList<>();
        step(("Генерируем тело запроса"), () -> {
            listIngredients.add(getRandomIngredient());
            listIngredients.add(getRandomIngredient());
            order.setIngredients(listIngredients);
        });
        step(("Отправить запрос POST /orders для создания заказа"), () -> {
            response = requestCustom.postOrders(order, token);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Отправить запрос GET /orders для получения информации о заказе"), () -> {
            response = requestCustom.getOrders(token);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
            checkResponseError(false, "You should be authorised");
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

    @Step("Получаем случайный ингредиент")
    private String getRandomIngredient() {
        response = requestCustom.getIngredients();
        List<Ingredient> ingredients = response.then().extract().body().jsonPath().getList("data", Ingredient.class);
        Random random = new Random();
        int r = random.nextInt(ingredients.size());
        return ingredients.get(r).get_id();
    }

    @Step("Проверяем полученный ответ")
    private void checkResponseOrderUserAuthorization() {
        response.then().assertThat().body("success", equalTo(true))
                .and().body("orders._id", notNullValue())
                .and().body("orders.number", notNullValue());
        List<String> ingredients = response.then().extract().body().jsonPath().getList("orders.ingredients");
        assertFalse(ingredients.isEmpty());
    }

    @Step
    private void checkResponseError(boolean expectSuccess, String expectMessage) {
        response.then().assertThat().body("success", equalTo(expectSuccess))
                .and().body("message", equalTo(expectMessage));
    }

}
