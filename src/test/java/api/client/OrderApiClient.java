package api.client;

import api.models.OrdersRequest;
import api.specs.ApiSpecs;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;

import java.util.List;

public class OrderApiClient {

    private final String baseURI;
    private final static String ORDERS = "/api/orders";
    private final static String GET_INGRIDIENTS = "api/ingredients";
    private static final Gson gson = new Gson();

    public OrderApiClient(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Создание заказа, пользователь авторизован")
    public ValidatableResponse createOrderWithAuth(String accessToken, List<String> ingredients) {
        String requestBody = gson.toJson(new OrdersRequest(ingredients));
        return ApiSpecs.requestSpec(baseURI)
                .header("Authorization", accessToken)
                .body(requestBody)
                .post(ORDERS)
                .then()
                .log().body();
    }

    @Step("Создание заказа, пользователь не авторизован")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredients) {
        String requestBody = gson.toJson(new OrdersRequest(ingredients));
        return ApiSpecs.requestSpec(baseURI)
                .body(requestBody)
                .post(ORDERS)
                .then()
                .log().body();
    }

    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse createOrderWithoutIngredients() {
        return ApiSpecs.requestSpec(baseURI)
                .body("{\"ingredients\":[]}")
                .post(ORDERS)
                .then()
                .log().body();
    }

    @Step("Получить заказы пользователя")
    public ValidatableResponse getUserOrders(String accessToken) {
        return ApiSpecs.requestSpec(baseURI)
                .header("Authorization", accessToken)
                .get(ORDERS)
                .then()
                .log().body();
    }

    @Step("Получить заказы, пользователь не авторизован")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return ApiSpecs.requestSpec(baseURI)
                .get(ORDERS)
                .then()
                .log().body();
    }

    @Step("Получить _id всех ингредиентов")
    public List<String> getIngredientIds() {
        ValidatableResponse response = ApiSpecs.requestSpec(baseURI)
                .get(GET_INGRIDIENTS)
                .then()
                .log().body();

        JsonPath jsonPath = response.extract().jsonPath();
        return jsonPath.getList("data._id");
    }
}
