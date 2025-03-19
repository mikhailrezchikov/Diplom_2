package api.tests;

import api.data.TestData;
import api.models.Credentials;
import api.models.User;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import api.client.UserApiClient;

@RunWith(Parameterized.class)
public class LoginUserTests extends TestBase {
    private final String email;
    private final String password;
    private final int expectedStatusCode;
    private final boolean expectedSuccess;
    private final String expectedMessage;
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private final UserApiClient userApiClient = new UserApiClient(BASE_URI);
    static TestData testData = new TestData();


    public LoginUserTests(String email, String password, int expectedStatusCode, boolean expectedSuccess, String expectedMessage) {
        this.email = email;
        this.password = password;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedSuccess = expectedSuccess;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {testData.email, testData.password, SC_OK, true, null},
                {testData.email, testData.password + "Test", SC_UNAUTHORIZED, false, "email or password are incorrect"},
                {testData.email + "Test", testData.password, SC_UNAUTHORIZED, false, "email or password are incorrect"},
        };
    }

    @Test
    public void loginUserTest() {

        createUserResponse = userApiClient.createUser(new User(testData.email, testData.password, "Test User"));
        Credentials credentials = new Credentials(email, password);
        ValidatableResponse loginUserResponse = userApiClient.loginUser(credentials);

        step("Проверка ответа", () -> {
            loginUserResponse.assertThat()
                    .statusCode(expectedStatusCode)
                    .body("success", equalTo(expectedSuccess));
            if (expectedMessage != null) {
                loginUserResponse.body("message", equalTo(expectedMessage));
            }
        });
    }
}
