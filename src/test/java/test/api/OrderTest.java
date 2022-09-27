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

public class OrderTest {
    private Response response;
    private RequestCustom requestCustom = new RequestCustom();
    private String emailUser = "emailTset@ya.ru";
    private String passwordUser = "tegt123";
    private String nameUser = "Lim";
    private String token;
    private String tokenLogin;

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Тестирование запроса POST /orders" +
            "<p> Запрос создает заказ  </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 200 </p>" +
            "<p> JSON с корректной структурой в теле ответа </p>" +
            "<p> Заказ создан </p>")
    public void testCreateOrderWithIngredients() {
        Order order = new Order();
        List<String> listIngredients = new ArrayList<>();
        step(("Генерируем тело запроса"), () -> {
            listIngredients.add(getRandomIngredient());
            listIngredients.add(getRandomIngredient());
            order.setIngredients(listIngredients);
        });
        step(("Отправить запрос POST /orders"), () -> {
            response = requestCustom.postOrders(order);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
            checkResponse();
        });
    }

    @Test
    @DisplayName("Создание заказа без ингредиентами")
    @Description("Тестирование запроса POST /orders" +
            "<p> Запрос создает заказ  </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 400 </p>" +
            "<p> Сообщение о ошибке в теле ответа </p>")
    public void testCreateOrder_WithoutIngredients() {
        Order order = new Order();
        List<String> listIngredients = new ArrayList<>();
        step(("Генерируем тело запроса"), () -> {
            order.setIngredients(listIngredients);
        });
        step(("Отправить запрос POST /orders"), () -> {
            response = requestCustom.postOrders(order);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_BAD_REQUEST);
            checkResponseError(false, "Ingredient ids must be provided");
        });
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Тестирование запроса POST /orders" +
            "<p> Запрос создает заказ  </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 500 </p>")
    public void testCreateOrder_IncorrectHashIngredients() {
        Order order = new Order();
        List<String> listIngredients = new ArrayList<>();
        step(("Генерируем тело запроса"), () -> {
            listIngredients.add("60d3b41abdacab0026a3c6");
            listIngredients.add("69646e4dc916e00276b287");
            order.setIngredients(listIngredients);
        });
        step(("Отправить запрос POST /orders"), () -> {
            response = requestCustom.postOrders(order);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        });
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Тестирование запроса POST /orders" +
            "<p> Запрос создает заказ  </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 200 </p>" +
            "<p> JSON с корректной структурой в теле ответа </p>" +
            "<p> Заказ создан </p>")
    public void testCreateOrderAuthorizationUser() {
        User user = new User(emailUser, passwordUser, nameUser);
        step(("Отправить запрос POST /auth/register для создания пользователя"), () -> {
            response = requestCustom.postCreateUser(user);
            token = getToken(response);
        });
        step(("Получили ответ"), () -> {
            checkStatus(HttpStatus.SC_OK);
        });
        step(("Отправить запрос POST /auth/login для авторизации пользователя"), () -> {
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
            checkResponseOrderAuthorizationUser(nameUser, emailUser);
        });
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    @Description("Тестирование запроса POST /orders" +
            "<p> Запрос создает заказ  </p>" +
            "<p> Проверяем: </p>" +
            "<p> Статус код 401 </p>" +
            "<p> JSON с корректной структурой в теле ответа </p>")
    public void testCreateOrder_Not_AuthorizationUser() {
        User user = new User(emailUser, passwordUser, nameUser);
        step(("Отправить запрос POST /auth/register для создания пользователя"), () -> {
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
            checkStatus(HttpStatus.SC_UNAUTHORIZED);
            checkResponseError(false, "You should be authorised");
        });
    }

    @Step("Получаем случайный ингредиент")
    private String getRandomIngredient() {
        response = requestCustom.getIngredients();
        List<Ingredient> ingredients = response.then().extract().body().jsonPath().getList("data", Ingredient.class);
        Random random = new Random();
        int r = random.nextInt(ingredients.size());
        return ingredients.get(r).get_id();
    }

    @Step("Проверяем, что статус код = {httpStatus}")
    private void checkStatus(int httpStatus) {
        assertThat(response.getStatusCode(), equalTo(httpStatus));
    }

    @Step("Проверяем тело ответа")
    private void checkResponse() {
        response.then().assertThat().body("success", equalTo(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
    }

    @Step("Проверяем тело ответа")
    private void checkResponseError(boolean expectSuccess, String expectMessage) {
        response.then().assertThat().body("success", equalTo(expectSuccess))
                .and().body("message", equalTo(expectMessage));
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
    private void checkResponseOrderAuthorizationUser(String expectUserName, String expectUserEmail) {
        response.then().assertThat().body("success", equalTo(true))
                .and().body("order._id", notNullValue())
                .and().body("order.owner.name", equalTo(expectUserName))
                .and().body("order.owner.email", equalTo(expectUserEmail.toLowerCase()))
                .and().body("order.status", notNullValue());
        List<Ingredient> ingredientsList = response.then().extract().body().jsonPath().getList("order.ingredients", Ingredient.class);
        assertFalse(ingredientsList.isEmpty());
    }
}
