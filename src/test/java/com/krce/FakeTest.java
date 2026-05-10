package com.krce;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class FakeTest {
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
    }

    @Test
    public void testgetproduct() {
        given().
                when().
                get("/products").
                then().
                statusCode(200).
                body("size()", Matchers.greaterThan(0));
    }

    @Test
    public void testgetprice() {
        given().
                queryParam("price", 100).
                when().
                get("/products").
                then().
                statusCode(200).
                body("[0].price", Matchers.equalTo(100));
    }

    @Test
    public void testgetcategories() {
        RestAssured.given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200).
                body("$", Matchers.instanceOf(List.class));
    }

    @Test
    public void testgettitle() {

        given().
                when().
                get("?title=Generic");
    }

    @Test
    public void testgetId() {
        given().
                pathParam("id", 1).
                when().
                get("/categories/{id}").
                then().
                statusCode(200).
                body("id", Matchers.equalTo(1));
    }

    @Test
    public void testgetpricerange() {
        given().
                queryParam("price_min", 100).
                queryParam("price_max", 1000).
                when()
                .get("/products/").
                then().
                statusCode(200).
                body("price", everyItem(allOf(Matchers.greaterThanOrEqualTo(100), lessThanOrEqualTo(1000))));
    }

    @Test
    public void testcatdata() {

        String uniqueName = "nanthan_2" + System.currentTimeMillis();

        Map<String, String> body = Map.of(
                "name", uniqueName,
                "image", "https://placehold.co/600x400"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/categories")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", Matchers.equalTo(uniqueName))
                .body("image", Matchers.equalTo("https://placehold.co/600x400"));
    }
}
