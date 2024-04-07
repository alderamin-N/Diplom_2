package api;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import order.Order;

import static api.BaseURL.URL;
import static io.restassured.RestAssured.given;

public class OrderAPI {
    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(requestSpec())
                .auth().oauth2(accessToken)
                .body(order).log().all()
                .when()
                .post("api/orders")
                .then().log().all();
    }

    @Step("Получить заказы конкретного пользователя")
    public ValidatableResponse getListOrder(String accessToken) {
        return given()
                .spec(requestSpec())
                .auth().oauth2(accessToken)
                .get("api/orders").then().log().all();

    }

}
