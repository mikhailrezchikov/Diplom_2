package api.tests;

import api.client.UserApiClient;
import api.models.OrdersResponse;
import api.models.UserResponse;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersTests extends TestBase {

    @Test
    public void getUserOrdersUserIsAuthorizedTest() {

        UserResponse createUserResponse = userApiClient.createUser(user)
                .extract()
                .as(UserResponse.class);

        userTokenId = createUserResponse.getAccessToken();

        List<String> ingredients = orderApiClient.getIngredientIds();

        orderApiClient.createOrderWithAuth(userTokenId, List.of(ingredients.get(0), ingredients.get(1)));

        OrdersResponse getOrderResponse = orderApiClient
                .getUserOrders(userTokenId)
                .statusCode(SC_OK)
                .extract()
                .as(OrdersResponse.class);

        step("Проверка ответа", () -> {
            Assert.assertTrue(getOrderResponse.isSuccess());

            OrdersResponse.Order order = getOrderResponse.getOrders().get(0);
            List<String> orderIngredients = order.getIngredients();

            String firstIngredientInOrder = orderIngredients.get(0);
            String secondIngredientInOrder = orderIngredients.get(1);

            Assert.assertEquals("Ответ не соответствует ожидаемому", ingredients.get(0), firstIngredientInOrder);
            Assert.assertEquals("Ответ не соответствует ожидаемому", ingredients.get(1), secondIngredientInOrder);
        });
    }

    @Test
    public void getUserOrdersUserIsNotAuthorizedTest() {

        ValidatableResponse getOrderResponse = orderApiClient.getUserOrdersWithoutAuth();

        step("Проверка ответа", () -> {
            getOrderResponse.statusCode(SC_UNAUTHORIZED)
                    .body("success", equalTo(false))
                    .body("message", equalTo("You should be authorised"));
        });
    }
}
