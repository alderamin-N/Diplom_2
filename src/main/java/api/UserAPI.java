package api;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import user.User;

import static api.BaseURL.URL;
import static io.restassured.RestAssured.given;

public class UserAPI {

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }


    @Step("Создание/регистрация нового user")
    public ValidatableResponse createUser(User user){
        return given()
                .spec(requestSpec())
                .body(user)
                .when()
                .post("api/auth/register").then().log().all();
    }

    @Step("Авторизация user в системе")
    public ValidatableResponse loginUser(User user){
        return given()
                .spec(requestSpec())
                .body(user).log().all()
                .when()
                .post("api/auth/login").then();
    }

    @Step("Удаление user")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(requestSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete("api/auth/user").then().log().all();
    }


    @Step("Выход user из системы")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(requestSpec())
                .body(refreshToken)
                .when()
                .post("api/auth/logout").then();
    }
    @Step("Редактирование user метод PATCH")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given()
                .spec(requestSpec())
               // .auth().oauth2(accessToken)
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .patch("api/auth/user").then().log().all();
    }

}
