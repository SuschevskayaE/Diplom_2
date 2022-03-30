package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class LoginUserTests {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        stellarburgersApiClient = new AuthStellarburgersApiClient();
        user = User.getRandom();

        ValidatableResponse response = stellarburgersApiClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        stellarburgersApiClient.deleteUser(token).assertThat().statusCode(202)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void loginUserSuccess() {
        stellarburgersApiClient.loginUser(user).assertThat().statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());

    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    public void loginUserPasswordIncorrectFail() {
        user.setPassword("invalid password");
        stellarburgersApiClient.loginUser(user).assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректным email")
    public void loginUserEmailIncorrectFail() {
        user.setEmail("invalidEmail@ya.ru");
        stellarburgersApiClient.loginUser(user).assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с пустым паролем")
    public void loginUserPasswordEmptyFail() {
        user.setPassword("");
        stellarburgersApiClient.loginUser(user).assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с пустым email")
    public void loginUserEmailEmptyFail() {
        user.setEmail("");
        stellarburgersApiClient.loginUser(user).assertThat().statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }
}
