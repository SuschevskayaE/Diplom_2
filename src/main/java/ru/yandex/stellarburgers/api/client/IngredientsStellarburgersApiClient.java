package ru.yandex.stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.stellarburgers.api.constants.Endpoints;

import static io.restassured.RestAssured.given;

public class IngredientsStellarburgersApiClient extends StellarburgersApiClient {
    public final String PATH = Endpoints.BASE_URL + Endpoints.INGREDIENTS_URL;

    @Step("Ingredients - Получение данных об ингредиентах")
    public Response getIngredients() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(PATH);
    }
}
