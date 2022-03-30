package ru.yandex.stellarburgers.api.models;

import com.github.javafaker.Faker;
import lombok.*;


@Data
@AllArgsConstructor
public class User {

    public String email;
    public String password;
    public String name;

    public static User getRandom() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.lorem().characters(10, true);
        String name = faker.name().name();
        User user = new User(email, password, name);
        return user;
    }

}
