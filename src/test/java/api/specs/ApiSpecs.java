package api.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiSpecs {
    public static RequestSpecification requestSpec(String baseURI) {
        return given()
                .filter(new AllureRestAssured())
                .log().uri()
                .log().method()
                .log().body()
                .baseUri(baseURI)
                .header("Content-Type", "application/json");
    }
}