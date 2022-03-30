package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.is;

public class ChangeUserAlreadyExistsTest {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;
    private User userChange;
    private String token;
    private String tokenChange;

    @Before
    public void setUp() {
        stellarburgersApiClient = new AuthStellarburgersApiClient();
        user = User.getRandom();
        userChange = User.getRandom();

        ValidatableResponse response = stellarburgersApiClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat().statusCode(200)
                .and()
                .body("success", is(true));

        ValidatableResponse responseChange = stellarburgersApiClient.createUser(userChange);
        tokenChange = responseChange.extract().path("accessToken");
        responseChange.assertThat().statusCode(200)
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

        stellarburgersApiClient.deleteUser(tokenChange).assertThat().statusCode(202)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Обновление данных о пользователе. Email уже существует")
    public void changeUserEmailExistsFail() {
        userChange.setEmail(user.email);
        stellarburgersApiClient.changeUser(userChange, tokenChange).assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("User with such email already exists"));

    }
}
