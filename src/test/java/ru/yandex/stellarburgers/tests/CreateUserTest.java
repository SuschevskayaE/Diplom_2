package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        stellarburgersApiClient = new AuthStellarburgersApiClient();
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
    @DisplayName("Создание пользователя")
    public void createUserSuccess() {
        user = User.getRandom();

        ValidatableResponse response = stellarburgersApiClient.createUser(user);
        token = response.extract().path("accessToken");

        response.assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
}
