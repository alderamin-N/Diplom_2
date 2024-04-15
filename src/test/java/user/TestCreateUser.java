package user;

import api.UserAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.notNullValue;



public class TestCreateUser {

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
    public void tearDown(){
        if (accessToken != null){
            userAPI.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Create new user")
    @Description("Создание нового уникального  пользователя")
    public void createNewUserTest(){
        ValidatableResponse response = userAPI.createUser(user);
        response.assertThat().body("accessToken", notNullValue())
                .and().statusCode(200);
        accessToken = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
    }

    @Test
    @DisplayName("Create again user")
    @Description("Создание пользователя, который уже зарегистрирован")
    public void createAgainUserTest(){
        user = RandomUser.userRandom();
        ValidatableResponse response = userAPI.createUser(user);
        accessToken = response.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
        response = userAPI.createUser(user);
        String message = response.extract().path("message");
        response.statusCode(403);
        Assert.assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Create user without email")
    @Description("Создание пользователя без почты")
    public void createUserWithoutEmailTest(){
        user = RandomUser.userRandom();
        user.setEmail("");
        ValidatableResponse response = userAPI.createUser(user);
        String message = response.extract().path("message");
        response.statusCode(403);
        Assert.assertEquals("Email, password and name are required fields", message);
    }
    @Test
    @DisplayName("Create user without password")
    @Description("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest(){
        user = RandomUser.userRandom();
        user.setPassword("");
        ValidatableResponse response = userAPI.createUser(user);
        String message = response.extract().path("message");
        response.statusCode(403);
        Assert.assertEquals("Email, password and name are required fields", message);
    }
    @Test
    @DisplayName("Create user without name")
    @Description("Создание пользователя без пароля")
    public void createUserWithoutNameTest(){
        user = RandomUser.userRandom();
        user.setName("");
        ValidatableResponse response = userAPI.createUser(user);
        String message = response.extract().path("message");
        response.statusCode(403);
        Assert.assertEquals("Email, password and name are required fields", message);
    }

}
