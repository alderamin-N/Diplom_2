package order;

import api.OrderAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SimpleTimeZone;

public class OrderList {

    public static Order withIngredients() {
        List<String> list = OrderAPI.сreateListIngredients();
        String[] ingredients = list.toArray(new String[0]);
        return new Order(ingredients);
    }

    public static Order invalidHash(){
        final String[] ingredients = {"p1c0c5a71d1f82001bdaaa75", "o1c0c5a71d1f82001bdaaa6d"};
        return new Order(ingredients);
    }

}
