package ru.praktikum;

import io.restassured.RestAssured;
import org.junit.Before;

public abstract class BaseTest {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }
}
