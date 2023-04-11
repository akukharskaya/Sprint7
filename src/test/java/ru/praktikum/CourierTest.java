package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static ru.praktikum.CourierHelper.*;


public class CourierTest extends BaseTest {
    //Тесты на регистрацию
    @Test
    @DisplayName("Create Courier - success")
    public void testCreateCourier_success() {
        var courier = randomCourier();

        create(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", equalTo(true));

        delete(courier);
    }

    @Test
    @DisplayName("Create Courier - fail If Courier Exists")
    public void testCreateCourier_failIfCourierExists() {
        var courier = randomCourier();
        create(courier);

        create(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Create Courier - fail On Empty Login In Registration")
    public void testCreateCourier_failOnEmptyLoginInRegistration() {
        Courier courier = new Courier("", "_", "_");

        ValidatableResponse response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(PATH_REGISTRATION)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    //Тесты на авторизацию
    @Test
    @DisplayName("Login - success")
    public void testLogin_success() {
        Courier courier = getExistingCourier();

        login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Login - fail On Incorrect Password")
    public void testLogin_failOnIncorrectPassword() {
        Courier courier = getExistingCourier();
        courier.setPassword("wrong pass");

        ValidatableResponse response = login(courier)
                        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Login - fail On Login Is Empty")
    public void testLogin_failOnLoginIsEmpty() {
        Courier courier = getExistingCourier();
        courier.setLogin("");

        ValidatableResponse response = login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Login fail On Courier Not Exists")
    public void testLogin_failOnCourierNotExists() {
        Courier courier = randomCourier();

        ValidatableResponse response = login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }




}
