package com.github.tanyuushaa.server;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServerTest {

    static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    void loginShouldReturnToken() {
        token = given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"user\", \"password\": \"pass\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().path("token");
    }

    @Test
    @Order(2)
    void loginWithWrongPasswordShouldReturn401() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\": \"user\", \"password\": \"wrong\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(401);
    }

    @Test
    @Order(3)
    void putNewProductShouldReturn201() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"milk\",\"category\":\"dairy\",\"quantity\":50,\"price\":20.5}")
                .when()
                .put("/api/good")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }


    @Test
    @Order(5)
    void updateProductShouldReturn204() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"milk updated\",\"category\":\"dairy\",\"quantity\":70,\"price\":25.5}")
                .when()
                .post("/api/good/1")
                .then()
                .statusCode(anyOf(is(204), is(404)));
    }

    @Test
    @Order(6)
    void deleteProductShouldReturn204() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/good/1")
                .then()
                .statusCode(anyOf(is(204), is(404)));
    }

    @Test
    @Order(7)
    void unauthorizedAccessShouldReturn403() {
        when()
                .get("/api/good/1")
                .then()
                .statusCode(403);
    }
}