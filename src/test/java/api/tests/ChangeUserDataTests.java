package api.tests;

import api.models.UserResponse;
import api.models.User;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserDataTests extends TestBase {

    @Test
    public void changeUserDataSuccessfulTest() {

        UserResponse createUserResponse = userApiClient.createUser(user)
                .extract()
                .as(UserResponse.class);

        userTokenId = createUserResponse.getAccessToken();

        User updateUser = new User(
                user.getEmail() + random.nextInt(100),
                user.getPassword(),
                user.getName() + random.nextInt(100)
        );

        UserResponse updateUserResponse = userApiClient.updateUser(updateUser, userTokenId)
                .statusCode(SC_OK)
                .extract()
                .as(UserResponse.class);


        step("Проверка ответа", () -> {
            Assert.assertTrue("Ответ не соответствует ожидаемому" ,updateUserResponse.isSuccess());
            Assert.assertEquals("Ответ не соответствует ожидаемому", updateUser.getEmail(), updateUserResponse.getUser().getEmail());
            Assert.assertEquals("Ответ не соответствует ожидаемому", updateUser.getName(), updateUserResponse.getUser().getName());
        });
    }

    @Test
    public void shouldNotChangeUserDataWithoutAuthorizationTest() {

        createUserResponse = userApiClient.createUser(user);

        User updateUser = new User(
                user.getEmail() + random.nextInt(100),
                user.getPassword(),
                user.getName() + random.nextInt(100)
        );

        ValidatableResponse updateUserWithoutAuthorizationResponse = userApiClient.updateUserWithoutAuthorizationToken(updateUser);

        step("Проверка ответа", () -> {
            updateUserWithoutAuthorizationResponse.statusCode(SC_UNAUTHORIZED)
                    .body("success", equalTo(false))
                    .body("message", equalTo("You should be authorised"));
        });
    }
}
