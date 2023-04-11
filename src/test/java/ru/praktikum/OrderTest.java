package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {
    private final OrderRequest request;
    private static final String PATH_ORDER = "/api/v1/orders";

    public OrderTest(OrderRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters
    public static Object[] parameters() {
        return new Object[]{
                new OrderRequest("Аня",
                        "Аня",
                        "Москва",
                        2,
                        "+79999999999",
                        2,
                        "2023-04-15",
                        null,
                        Set.of("BLACK")),

                new OrderRequest("А",
                        "А",
                        "Москва",
                        3,
                        "+78888888888",
                        4,
                        "2023-04-20",
                        "к 9-00",
                        Set.of("BLACK", "GREY")),

                new OrderRequest("Аррр",
                        "Аддд",
                        "Москва",
                        10,
                        "+78888888688",
                        4,
                        "2023-04-17",
                        "оплата наличными",
                        Set.of()),
        };
    }

    @Test
    @DisplayName("Order - success")
    public void testOrder_success() {
        given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post(PATH_ORDER)
                .then()
                .assertThat()
                .body("track", notNullValue());

    }
}
