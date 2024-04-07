package User;

import api.UserAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.RandomUser;
import user.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestLoginUser {
    private User user;
    private UserAPI userAPI;
    protected  final RandomUser randomUser = new RandomUser();
    private String accessToken;
    private String refreshToken;
    private ValidatableResponse responseRegister;
    @Before
    public void setUp(){
        userAPI = new UserAPI();
        user = randomUser.createRandomUser();
        responseRegister = userAPI.createUser(user);
    }
    @After
    public void tearDown(){
        if(refreshToken != null)
            userAPI.logoutUser(refreshToken);
        if (accessToken != null){
            userAPI.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Correct login")
    @Description("Логин под существующим пользователем")
    public void correctLoginUserTest(){
        accessToken = responseRegister.extract().path("accessToken");
        ValidatableResponse responseLogin = userAPI.loginUser(user);
        responseLogin.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Incorrect login")
    @Description("Логин с неверным логином и паролем")
    public void inCorrectLoginUserTest(){
        accessToken = responseRegister.extract().path("accessToken");
        user.setEmail("");
        ValidatableResponse response = userAPI.loginUser(user);
        response.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false)).body("message", is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Incorrect password")
    @Description("Логин с неверным логином и паролем")
    public void inCorrectPasswordUserTest() {
        accessToken = responseRegister.extract().path("accessToken");
        user.setPassword("");
        ValidatableResponse responseLogin = userAPI.loginUser(user);
        responseLogin.assertThat().statusCode(SC_UNAUTHORIZED).body("success", is(false)).body("message", is("email or password are incorrect"));
    }

}
