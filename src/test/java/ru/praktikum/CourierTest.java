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
    @DisplayName("Create Courier")
    public void testCreateCourier() {
        var courier = randomCourier();

        create(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", equalTo(true));

        delete(courier);
    }

    @Test
    @DisplayName("Create Courier Exists")
    public void testCreateCourierExists() {
        var courier = randomCourier();
        create(courier);

        create(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    @DisplayName("Create Courier With Empty Login In Registration")
    public void testCreateCourieWithEmptyLoginInRegistration() {
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
    @DisplayName("Login")
    public void testLogin() {
        Courier courier = getExistingCourier();

        login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Login With Incorrect Password")
    public void testLoginWithIncorrectPassword() {
        Courier courier = getExistingCourier();
        courier.setPassword("wrong pass");

        ValidatableResponse response = login(courier)
                        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Login With Login Is Empty")
    public void testLoginWithLoginIsEmpty() {
        Courier courier = getExistingCourier();
        courier.setLogin("");

        ValidatableResponse response = login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Login On Courier Not Exists")
    public void testLoginOnCourierNotExists() {
        Courier courier = randomCourier();

        ValidatableResponse response = login(courier)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }




}
