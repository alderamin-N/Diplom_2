package Order;

import api.OrderAPI;
import api.UserAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.RandomUser;
import user.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class TestGetListOrder {
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
    @DisplayName("Get list of order without authorization")
    @Description("Проверка получения листа заказа без авторизации")
    public void getListOrderTest(){
        order = OrderList.withIngredients();
        accessToken = "";
        ValidatableResponse responseCreateOrder = orderAPI.createOrder(order, accessToken);
        ValidatableResponse responseGetListOrder = orderAPI.getListOrder(accessToken);
        responseGetListOrder.assertThat().statusCode(401).body("success", is(false)).and().body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Get list of order with authorization")
    @Description("Проверка получения листа заказа с авторизацией")
    public void getListOrderUserTest(){
        user = randomUser.createRandomUser();
        responseRegister = userAPI.createUser(user);
        accessToken = responseRegister.extract().path("accessToken");
        StringBuilder sb = new StringBuilder(accessToken);
        sb.delete(0,7);
        accessToken = sb.toString();
        userAPI.loginUser(user);
        order = OrderList.withIngredients();
        orderAPI.createOrder(order, accessToken);
        orderAPI.createOrder(order, accessToken);
        orderAPI.createOrder(order, accessToken);
      ValidatableResponse responseGetListOrder = orderAPI.getListOrder(accessToken);
        boolean success = responseGetListOrder.extract().path("success");
        responseGetListOrder.statusCode(200);
        Assert.assertTrue(success);
    }





}
