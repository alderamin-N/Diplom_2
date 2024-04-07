package user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class RandomUser {
    public static User UserRandom(){
        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6,12);
        final String name = faker.name().firstName();
        return new User(email,password,name);
    }
    static Faker faker = new Faker();
    @Step("Создание нового user рандом")
    public User createRandomUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(6, 12),
                faker.name().firstName()
        );
    }

}
