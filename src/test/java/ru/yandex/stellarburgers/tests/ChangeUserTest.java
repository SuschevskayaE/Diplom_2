package ru.yandex.stellarburgers.tests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.User;

import static org.hamcrest.Matchers.is;

public class ChangeUserTest {
    private AuthStellarburgersApiClient stellarburgersApiClient;
    private User user;
    private String token;
    private Faker faker;

    @Before
    public void setUp() {
        stellarburgersApiClient = new AuthStellarburgersApiClient();
        user = User.getRandom();
        faker = new Faker();

        ValidatableResponse response = stellarburgersApiClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        stellarburgersApiClient.deleteUser(token).assertThat()
                .statusCode(202)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));
    }

    @Test
    @DisplayName("Обновление имени пользователя")
    public void changeNameUserSuccess() {
        String name = faker.name().name();
        user.setName(name);
        stellarburgersApiClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("user.name", is(name));

    }

    @Test
    @DisplayName("Обновление email пользователя")
    public void changeEmailUserSuccess() {
        String email = faker.internet().emailAddress();
        user.setEmail(email);
        stellarburgersApiClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("user.email", is(email));

    }

    @Test
    @DisplayName("Обновление password пользователя")
    public void changePasswordUserSuccess() {
        String password = faker.lorem().characters(10, true);
        user.setEmail(password);
        stellarburgersApiClient.changeUser(user, token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Обновление данных о пользователе без авторизации")
    public void changeUserWithoutAuthFail() {
        stellarburgersApiClient.changeUser(user).assertThat()
                .statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));

    }

    @Test
    @DisplayName("Обновление имени пользователя на пустое")
    public void changeEmptyNameUserFail() {
        user.setName("");
        stellarburgersApiClient.changeUser(user, token).assertThat()
                //приходит 200, но это должно быть ошибкой, так как создать пользователя с пустыми значениями нельзя
                .statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                //Текст ошибки может быть другим,но в документации не указан
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Обновление email пользователя на пустое")
    public void changeEmptyEmailUserFail() {
        user.setEmail("");
        stellarburgersApiClient.changeUser(user, token).assertThat()
                //приходит 200, но это должно быть ошибкой, так как создать пользователя с пустыми значениями нельзя
                .statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                //Текст ошибки может быть другим,но в документации не указан
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Обновление password пользователя на пустое")
    public void changeEmptyPasswordUserFail() {
        user.setPassword("");
        stellarburgersApiClient.changeUser(user, token).assertThat()
                //приходит 200, но это должно быть ошибкой, так как создать пользователя с пустыми значениями нельзя
                .statusCode(403)
                .and()
                .body("success", is(false))
                .and()
                //Текст ошибки может быть другим,но в документации не указан
                .body("message", is("Email, password and name are required fields"));
    }
}
