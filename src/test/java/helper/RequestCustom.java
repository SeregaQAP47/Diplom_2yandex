package helper;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Order;
import pojo.User;

import static io.restassured.RestAssured.given;

public class RequestCustom {
    private Response response;
    private final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    private static Object requestSpecification = new RequestSpecBuilder()
            .addFilter( new ResponseLoggingFilter())
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL).build();


    @Step("Запрос POST /auth/reqister")
    public Response postCreateUser(User user) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .post("/auth/register");
        return response;
    }

    @Step("Запрос DELETE /auth/user")
    public Response deleteUser(String token) {
        response = given()
//                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .auth().oauth2(token)
                .and()
                .when()
                .delete("/auth/user");
        return response;
    }

    @Step("Запрос DELETE /auth/user")
    public Response deleteUserTokenHeader(String token) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .and()
                .when()
                .delete("/auth/user");
        return response;
    }

    @Step("Запрос POST /auth/login")
    public Response postLoginUser(User user) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .and()
                .body(user)
                .when()
                .post("/auth/login");
        return response;
    }

    @Step("Запрос PATCH /auth/user")
    public Response patchUser(String token, User user) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .auth().oauth2(token)
                .and()
                .body(user)
                .when()
                .patch("/auth/user");
        return response;
    }

    @Step("Запрос GET /ingredients")
    public Response getIngredients() {
        response = given()
//                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .when()
                .get("/ingredients");
        return response;
    }

    @Step("Запрос POST /orders")
    public Response postOrders(Order order) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .and()
                .body(order)
                .when()
                .post("/orders");
        return response;
    }

    @Step("Запрос POST /orders")
    public Response postOrders(Order order, String token) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .auth().oauth2(token)
                .and()
                .body(order)
                .when()
                .post("/orders");
        return response;
    }

    @Step("Запрос GET /orders")
    public Response getOrders(String token) {
        response = given()
                .spec((RequestSpecification)requestSpecification)
                .baseUri(BASE_URL)
                .auth().oauth2(token)
                .when()
                .get("/orders");
        return response;
    }

}
