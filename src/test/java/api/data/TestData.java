package api.data;

import com.github.javafaker.Faker;

public class TestData {
    Faker faker = new Faker();
    public String email = faker.internet().emailAddress();
    public String name = faker.name().firstName();
    public String password = faker.number().digits(10);
}
