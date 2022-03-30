package ru.yandex.stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.stellarburgers.api.constants.Endpoints;
import ru.yandex.stellarburgers.api.models.User;

import static io.restassured.RestAssured.given;

public class AuthStellarburgersApiClient extends StellarburgersApiClient {

    public final String PATH = Endpoints.BASE_URL + Endpoints.AUTH_URL;

    @Step("auth/register - Создание пользователя {user}")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(PATH + "register")
                .then();
    }

    @Step("auth/login - Авторизация пользователя {user}")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(PATH + "login")
                .then();
    }

    @Step("auth/login - Обновление пользователя {user}")
    public ValidatableResponse changeUser(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .patch(PATH + "user")
                .then();
    }

    @Step("auth/login - Обновление пользователя {user}")
    public ValidatableResponse changeUser(User user, String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(PATH + "user")
                .then();
    }

    @Step("auth/user - Удаление пользователя {user}")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseReqSpec())
                .header("Authorization", token)
                .when()
                .delete(PATH + "user")
                .then();
    }
}
