package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.is;

public class CreateUserWithEmptyFieldsTest {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;

    @Before
    public void setUp() {
        stellarburgersApiClient = new AuthStellarburgersApiClient();
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithEmptyEmailFail() {
        user = new User("", "password", "Username");
        stellarburgersApiClient.createUser(user).assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без password")
    public void createUserWithEmptyPasswordFail() {
        user = new User("email@ya.ru", "", "Username");
        stellarburgersApiClient.createUser(user).assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithEmptyUsernameFail() {
        user = new User("email@ya.ru", "password", "");
        stellarburgersApiClient.createUser(user).assertThat().statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));

    }
}
