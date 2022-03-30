package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.is;

public class CreateUserAlreadyExistsTest {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;
    private String token;
    private String actualToken;

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
        if(actualToken != null){
            stellarburgersApiClient.deleteUser(actualToken).assertThat().statusCode(202)
                    .and()
                    .body("success", is(true))
                    .and()
                    .body("message", is("User successfully removed"));
        }
        stellarburgersApiClient.deleteUser(token).assertThat().statusCode(202)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Создание существующего пользователя")
    public void createUserAlreadyExistsFail() {
        ValidatableResponse response = stellarburgersApiClient.createUser(user);

        int statusCode = response.extract().statusCode();
        if (statusCode == 200) {
            actualToken = response.extract().path("accessToken");
        }

        response.assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("User already exists"));

    }

}
