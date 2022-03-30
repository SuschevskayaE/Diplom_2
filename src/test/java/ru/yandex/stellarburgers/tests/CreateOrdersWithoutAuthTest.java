package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.IngredientsStellarburgersApiClient;
import ru.yandex.stellarburgers.api.client.OrdersStellarburgersApiClient;
import ru.yandex.stellarburgers.api.models.Ingredient;
import ru.yandex.stellarburgers.api.models.IngredientsRequest;
import ru.yandex.stellarburgers.api.models.IngredientsResponse;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrdersWithoutAuthTest {
    private OrdersStellarburgersApiClient ordersStellarburgersApiClient;
    private IngredientsStellarburgersApiClient ingredientsStellarburgersApiClient;
    private IngredientsRequest ingredientsRequest;
    private Response response;

    @Before
    public void setUp() {

        ordersStellarburgersApiClient = new OrdersStellarburgersApiClient();
        ingredientsStellarburgersApiClient = new IngredientsStellarburgersApiClient();

        response = ingredientsStellarburgersApiClient.getIngredients();
        response.then().assertThat().statusCode(200);
        Ingredient[] getIngredients = response.body().as(IngredientsResponse.class).getData();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(getIngredients[0]._id);
        ingredients.add(getIngredients[1]._id);

        ingredientsRequest = new IngredientsRequest(ingredients);

    }
    //TODO Удалить заказ. В документации нет

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthSuccess() {
        ordersStellarburgersApiClient.createOrders(ingredientsRequest).assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue());
    }

}
