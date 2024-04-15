package user;

import api.UserAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class TestUpdateUser {

    private User user;
    private UserAPI userAPI;
    protected  final RandomUser randomUser = new RandomUser();
    private String accessToken;

    @Before
    public void setUp(){
        userAPI = new UserAPI();
        user = randomUser.createRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            userAPI.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Change user's data with authorization")
    @Description("Изменения данных существующего пользователя с авторизацией")
    public void changeDataUserWithAuthorization() {
        ValidatableResponse response = userAPI.createUser(user);
        accessToken = response.extract().path("accessToken");
        User changedUser = randomUser.createRandomUser();
        ValidatableResponse responseUpdate = userAPI.updateUser(accessToken, changedUser);
        responseUpdate.assertThat().statusCode(SC_OK).body("success", is(true));
    }


    @Test
    @DisplayName("Change user's data without authorization")
    @Description("Изменения данных пользователя без авторизации")
    public void changeDataUserWithoutAuthorization() {
        ValidatableResponse responseRegister = userAPI.createUser(user);
        accessToken = responseRegister.extract().path("accessToken");
        User changedUser = randomUser.createRandomUser();
        accessToken = "";
        ValidatableResponse responsePatch = userAPI.updateUser(accessToken, changedUser);
        responsePatch.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false)).and().body("message", is("You should be authorised"));
    }

}
