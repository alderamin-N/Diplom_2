package order;

import api.OrderAPI;
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


import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCreateOrder {

    private User user;
    private UserAPI userAPI;
    protected  final RandomUser randomUser = new RandomUser();
    private String accessToken;
    private Order order;
    private OrderAPI orderAPI;
    private ValidatableResponse responseRegister;

    @Before
    public void setUp(){
        userAPI = new UserAPI();
        orderAPI = new OrderAPI();
    }

    @After
    public void tearDown(){
        if (accessToken != null){
            userAPI.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Create order with authorization")
    @Description("Проверка создания заказа с авторизацией")
    public void createOrderWithAuthorizationTest() {
        user = randomUser.createRandomUser();
        responseRegister = userAPI.createUser(user);
        String accessToken = responseRegister.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
        order = OrderList.withIngredients();
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        boolean success = responseRegister.extract().path("success");
        responseCreateOrder.statusCode(200);
        Assert.assertTrue(success);
    }

    @Test
    @DisplayName("Create order with authorization and without ingridients")
    @Description("Проверка создания заказа с авторизацией без ингредиентов")
    public void createOrderWithAuthorizationandWithoutIngridientTest() {
        user = randomUser.createRandomUser();
        responseRegister = userAPI.createUser(user);
        String accessToken = responseRegister.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
        order = OrderList.withIngredients();
        order.setIngredients(null);
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST).body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with authorization and with invalid hash ingridients")
    @Description("Проверка создания заказа с авторизацией и с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationandWithInvalidHashTest() {
        user = randomUser.createRandomUser();
        responseRegister = userAPI.createUser(user);
        String accessToken = responseRegister.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
        order = OrderList.invalidHash();
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        responseCreateOrder.statusCode(500);
    }

    @Test
    @DisplayName("Create order without authorization")
    @Description("Проверка создания заказа без авторизацией")
    public void createOrderWithoutAuthorizationTest() {
        order = OrderList.withIngredients();
        accessToken = "";
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        responseCreateOrder.assertThat().body("order.number",notNullValue()).and().statusCode(200);
    }

    @Test
    @DisplayName("Create order without ingridients")
    @Description("Проверка создания заказа без ингридиентов")
    public void createOrderWithoutIngridientTest() {
        order = OrderList.withIngredients();
        order.setIngredients(null);
        accessToken = "";
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST).body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with invalid hash ingridients")
    @Description("Проверка создания заказа с неверным хешем ингридиентов")
    public void createOrderWithInvalidHashTest(){
        order = OrderList.invalidHash();
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, "");
        responseCreateOrder.statusCode(500);
    }


}
