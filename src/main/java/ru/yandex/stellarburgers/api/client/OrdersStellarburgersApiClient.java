package ru.yandex.stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.stellarburgers.api.constants.Endpoints;
import ru.yandex.stellarburgers.api.models.IngredientsRequest;

import static io.restassured.RestAssured.given;

public class OrdersStellarburgersApiClient extends StellarburgersApiClient {

    public final String PATH = Endpoints.BASE_URL + Endpoints.ORDERS_URL;

    @Step("orders - Создание заказа")
    public ValidatableResponse createOrders(IngredientsRequest ingredientsRequest, String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .body(ingredientsRequest)
                .when()
                .post(PATH)
                .then();
    }

    @Step("orders - Создание заказа")
    public ValidatableResponse createOrders(IngredientsRequest ingredientsRequest) {
        return given()
                .spec(getBaseReqSpec())
                .body(ingredientsRequest)
                .when()
                .post(PATH)
                .then();
    }

    @Step("orders - Получить заказы")
    public ValidatableResponse getOrders(String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .when()
                .get(PATH)
                .then();
    }

    @Step("orders - Получить заказы")
    public ValidatableResponse getOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(PATH)
                .then();
    }
}
