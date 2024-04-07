package order;

public class OrderList {

    public static Order withIngredients() {
        final String[] ingredients = {"61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa6d"};
        return new Order(ingredients);
    }

    public static Order invalidHash(){
        final String[] ingredients = {"p1c0c5a71d1f82001bdaaa75", "o1c0c5a71d1f82001bdaaa6d"};
        return new Order(ingredients);
    }

}
