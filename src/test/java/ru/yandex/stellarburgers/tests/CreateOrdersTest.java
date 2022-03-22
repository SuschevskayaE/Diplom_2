package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.AuthStellarburgersApiClient;
import ru.yandex.stellarburgers.api.client.IngredientsStellarburgersApiClient;
import ru.yandex.stellarburgers.api.client.OrdersStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.Ingredient;
import ru.yandex.stellarburgers.api.models.IngredientsResponse;
import ru.yandex.stellarburgers.api.models.IngredientsRequest;
import ru.yandex.stellarburgers.api.models.User;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrdersTest {
    private OrdersStellarburgersApiClient ordersStellarburgersApiClient;
    private AuthStellarburgersApiClient authStellarburgersApiClient;
    private IngredientsStellarburgersApiClient ingredientsStellarburgersApiClient;
    private User user;
    private IngredientsRequest ingredientsRequest;
    private String token;
    private Response response;

    @Before
    public void setUp() {

        ordersStellarburgersApiClient = new OrdersStellarburgersApiClient();
        authStellarburgersApiClient = new AuthStellarburgersApiClient();
        ingredientsStellarburgersApiClient = new IngredientsStellarburgersApiClient();
        user = User.getRandom();

        ValidatableResponse response = authStellarburgersApiClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true));
    }

    @After
    public void tearDown() {
        authStellarburgersApiClient.deleteUser(token).assertThat()
                .statusCode(202)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));

        //TODO Удалить заказ. В документации нет
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrderWithAuthSuccess() {
        response = ingredientsStellarburgersApiClient.getIngredients();
        response.then().assertThat().statusCode(200);
        Ingredient[] getIngredients = response.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);
        ingredients.add(getIngredients[2]._id);
        ingredientsRequest = new IngredientsRequest(ingredients);

        ordersStellarburgersApiClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с невалидным хеш ингредиента")
    public void createOrderInvalidIngredientHashFail() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("1234567");
        ingredientsRequest = new IngredientsRequest(ingredients);

        ordersStellarburgersApiClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createEmptyOrderFail() {
        ingredientsRequest = new IngredientsRequest(new ArrayList<>());
        ordersStellarburgersApiClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(400)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }
}
