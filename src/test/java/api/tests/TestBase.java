package api.tests;

import api.client.OrderApiClient;
import api.client.UserApiClient;
import api.data.TestData;
import api.models.User;
import api.models.UserResponse;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;

import java.security.SecureRandom;

import static org.apache.http.HttpStatus.*;

public class TestBase {
    protected SecureRandom random = new SecureRandom();

    protected static User user;
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final UserApiClient userApiClient = new UserApiClient(BASE_URI);
    protected final OrderApiClient orderApiClient = new OrderApiClient(BASE_URI);
    protected String userTokenId;
    ValidatableResponse createUserResponse;

    @Before
    public void before() {

        TestData testData = new TestData();

        user = new User(
                testData.email,
                testData.password,
                testData.name
        );
    }

    @After
    public void after() {

        if (userTokenId == null && createUserResponse != null && createUserResponse.extract().statusCode() == SC_OK) {
            UserResponse userResponse = createUserResponse.extract().as(UserResponse.class);
            userTokenId = userResponse.getAccessToken();
        }

        if (userTokenId != null) {
            userApiClient.deleteUser(userTokenId);
            userTokenId = null;
        }
    }
}
