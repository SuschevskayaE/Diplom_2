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
import ru.yandex.stellarburgers.api.models.IngredientsRequest;
import ru.yandex.stellarburgers.api.models.IngredientsResponse;
import ru.yandex.stellarburgers.api.models.User;


import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {
    private OrdersStellarburgersApiClient ordersStellarburgersApiClient;
    private AuthStellarburgersApiClient authStellarburgersApiClient;
    private IngredientsStellarburgersApiClient ingredientsStellarburgersApiClient;
    private User user;
    private String token;
    private IngredientsRequest ingredientsRequest;

    @Before
    public void setUp() {
        ordersStellarburgersApiClient = new OrdersStellarburgersApiClient();
        authStellarburgersApiClient = new AuthStellarburgersApiClient();
        ingredientsStellarburgersApiClient = new IngredientsStellarburgersApiClient();

        user = User.getRandom();


        ValidatableResponse responseUser = authStellarburgersApiClient.createUser(user);
        token = responseUser.extract().path("accessToken");
        responseUser.assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true));

        Response responseIngredients = ingredientsStellarburgersApiClient.getIngredients();
        responseIngredients.then().assertThat().statusCode(200);
        Ingredient[] getIngredients = responseIngredients.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);
        ingredientsRequest = new IngredientsRequest(ingredients);

        ordersStellarburgersApiClient.createOrders(ingredientsRequest, token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
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
    @DisplayName("Получить заказы пользователя")
    public void getOrderWithAuthSuccess() {
        ordersStellarburgersApiClient.getOrders(token).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("orders", notNullValue());
    }


}
