package api.tests;

import api.models.UserResponse;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTests extends TestBase {

    @Test
    public void createOrderWithAuthSuccessfulTest() {

        UserResponse createUserResponse = userApiClient.createUser(user)
                .extract()
                .as(UserResponse.class);

        userTokenId = createUserResponse.getAccessToken();

        List<String> ingredients = orderApiClient.getIngredientIds();

        ValidatableResponse createOrderResponse = orderApiClient
                .createOrderWithAuth(userTokenId, ingredients);

        step("Проверка ответа", () -> {
            createOrderResponse.assertThat()
                    .statusCode(SC_OK)
                    .body("success", equalTo(true))
                    .body("order.number", is(notNullValue()));
        });
    }

    @Test
    public void createOrderWithoutAuthSuccessfulTest() {

        List<String> ingredients = orderApiClient.getIngredientIds();

        ValidatableResponse createOrderResponse = orderApiClient
                .createOrderWithoutAuth(ingredients);

        step("Проверка ответа", () -> {
            createOrderResponse.assertThat()
                    .statusCode(SC_OK)
                    .body("success", equalTo(true))
                    .body("order.number", is(notNullValue()));
        });
    }

    @Test
    public void shouldNotCreateOrderWithoutIngredientsTest() {

        ValidatableResponse createOrderResponse = orderApiClient
                .createOrderWithoutIngredients();

        step("Проверка ответа", () -> {
            createOrderResponse.assertThat()
                    .statusCode(SC_BAD_REQUEST)
                    .body("success", equalTo(false))
                    .body("message", equalTo( "Ingredient ids must be provided"));
        });
    }

    @Test
    public void shouldNotCreateOrderWithInvalidIngridientsHashTest() {

        List<String> ingredients = orderApiClient.getIngredientIds();

        ValidatableResponse createOrderResponse = orderApiClient
                .createOrderWithoutAuth(List.of(ingredients.get(0) + "Test", ingredients.get(1) + "Test"));

        step("Проверка ответа", () -> {
            createOrderResponse.assertThat()
                    .statusCode(SC_INTERNAL_SERVER_ERROR);
        });
    }
}
