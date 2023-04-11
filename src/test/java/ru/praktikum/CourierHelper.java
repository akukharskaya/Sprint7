package ru.praktikum;

import io.restassured.response.Response;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class CourierHelper {
    public static final String PATH_REGISTRATION = "/api/v1/courier";
    public static final String PATH_AUTH = "/api/v1/courier/login";
    public static final String PATH_DELETE_COURIER = "/api/v1/courier/{id}";

    public static final String EXISTING_USER = "Ashot";
    public static final String EXISTING_USER_PASS = "Ashot";

    static Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH_REGISTRATION);
    }

    static Response delete(Courier courier) {
        if (EXISTING_USER.equals(courier.getLogin()))
            return null;

        var id = login(courier)
                .jsonPath().getString("id");

        return given()
                .header("Content-type", "application/json")
                .pathParam("id", id)
                .when()
                .delete(PATH_DELETE_COURIER);
    }

    static Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new LoginRequest(courier.getLogin(), courier.getPassword()))
                .when()
                .post(PATH_AUTH);
    }

    static Courier randomCourier() {
        String login = UUID.randomUUID().toString();
        return new Courier(login, "pass-" + login, "Anna " + login);
    }

    static Courier getExistingCourier() {
        return new Courier(EXISTING_USER, EXISTING_USER_PASS);
    }
}
