package api.tests;

import api.models.User;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTests extends TestBase {

    @Test
    public void createUserSuccessfulTest() {

        createUserResponse = userApiClient.createUser(user);

        step("Проверка ответа", () -> {
            createUserResponse.assertThat()
                    .statusCode(SC_OK)
                    .body("success", equalTo(true));
        });
    }

    @Test
    public void shouldNotCreateDuplicateUserTest() {

        createUserResponse = userApiClient.createUser(user);
        ValidatableResponse createDuplicateUserResponse = userApiClient.createUser(user);

        step("Проверка ответа", () -> {
            createDuplicateUserResponse.assertThat()
                    .statusCode(SC_FORBIDDEN)
                    .body("success", equalTo(false))
                    .body("message", equalTo("User already exists"));
        });
    }

    @Test
    public void shouldNotCreateUserWithoutEmailTest() {

        User userWithoutEmail = new User(
                null,
                user.getPassword(),
                user.getName()
        );

        createUserResponse = userApiClient.createUser(userWithoutEmail);

        step("Проверка ответа", () -> {
            createUserResponse.assertThat()
                    .statusCode(SC_FORBIDDEN)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Email, password and name are required fields"));
        });
    }

    @Test
    public void shouldNotCreateUserWithoutPasswordTest() {

        User userWithoutEmail = new User(
                user.getEmail(),
                null,
                user.getName()
        );

        createUserResponse = userApiClient.createUser(userWithoutEmail);

        step("Проверка ответа", () -> {
            createUserResponse.assertThat()
                    .statusCode(SC_FORBIDDEN)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Email, password and name are required fields"));
        });
    }

    @Test
    public void shouldNotCreateUserWithoutNameTest() {

        User userWithoutEmail = new User(
                user.getEmail(),
                user.getPassword(),
                null
        );

        createUserResponse = userApiClient.createUser(userWithoutEmail);

        step("Проверка ответа", () -> {
            createUserResponse.assertThat()
                    .statusCode(SC_FORBIDDEN)
                    .body("success", equalTo(false))
                    .body("message", equalTo("Email, password and name are required fields"));
        });
    }
}
