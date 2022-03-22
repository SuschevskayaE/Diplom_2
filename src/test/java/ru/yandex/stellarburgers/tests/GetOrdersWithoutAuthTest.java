package ru.yandex.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.api.client.OrdersStellarburgersApiClient;

import static org.hamcrest.Matchers.is;

public class GetOrdersWithoutAuthTest {
    private OrdersStellarburgersApiClient ordersStellarburgersApiClient;

    @Before
    public void setUp() {

        ordersStellarburgersApiClient = new OrdersStellarburgersApiClient();
    }

    @Test
    @DisplayName("Получить заказы пользователя без авторизации")
    public void getOrderWithoutAuthFail() {

        ordersStellarburgersApiClient.getOrders().assertThat()
                .statusCode(401)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }
}
