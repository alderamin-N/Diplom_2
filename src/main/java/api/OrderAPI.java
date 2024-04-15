package api;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import order.Order;

import java.util.ArrayList;

import static api.BaseURL.URL;
import static io.restassured.RestAssured.given;

public class OrderAPI {
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(BaseURL.requestSpec())
                .auth().oauth2(accessToken)
                .body(order).log().all()
                .when()
                .post(Endpoints.CREATE_ORDERS)
                .then().log().all();
    }

    @Step("Получить заказы конкретного пользователя")
    public ValidatableResponse getListOrder(String accessToken) {
        return given()
                .spec(BaseURL.requestSpec())
                .auth().oauth2(accessToken)
                .get(Endpoints.GET_ORDERS).then().log().all();

    }


    @Step("Получение ингредиентов")
    public static ValidatableResponse getAllIngredients() {
        return given()
                .spec(BaseURL.requestSpec())
                .get(Endpoints.INGREDIENT_API)
                .then()
                .statusCode(200);
    }


    @Step("Создание списка ингредиентов")
    public static ArrayList<String> сreateListIngredients()  {
        return new ArrayList<>(getAllIngredients()
                .extract()
                .path("data._id"));
    }

}
