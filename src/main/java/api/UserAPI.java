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
    @Step("Создание/регистрация нового user")
    public ValidatableResponse createUser(User user){
        return given()
                .spec(BaseURL.requestSpec())
                .body(user)
                .when()
                .post(Endpoints.REGISTER_USER_API).then().log().all();
    }

    @Step("Авторизация user в системе")
    public ValidatableResponse loginUser(User user){
        return given()
                .spec(BaseURL.requestSpec())
                .body(user).log().all()
                .when()
                .post(Endpoints.LOGIN_USER_API).then();
    }

    @Step("Удаление user")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(BaseURL.requestSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(Endpoints.DELETE_USER_API).then().log().all();
    }


    @Step("Выход user из системы")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(BaseURL.requestSpec())
                .body(refreshToken)
                .when()
                .post(Endpoints.LOGOUT_USER_API).then();
    }
    @Step("Редактирование user метод PATCH")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given()
                .spec(BaseURL.requestSpec())
               // .auth().oauth2(accessToken)
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .patch(Endpoints.PATCH_USER_API).then().log().all();
    }

}
