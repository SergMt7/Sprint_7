package model;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
public class CourierClient extends Client {
    protected final CourierGenerator generator = new CourierGenerator();
    private static final String CREATE = "/api/v1/courier";
    private static final String LOGIN = "api/v1/courier/login";
    private static final String DELETE = "api/v1/courier/";
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .when()
                .body(courier)
                .post(CREATE)
                .then().log().all();
    }
    public ValidatableResponse login(Login login) {
        return given()
                .spec(getSpec())
                .when()
                .body(login)
                .post(LOGIN)
                .then().log().all();
    }
    public ValidatableResponse delete(String id) {
        return given()
                .spec(getSpec())
                .when()
                .delete(DELETE + id)
                .then();
    }
}


