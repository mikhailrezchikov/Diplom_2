package api.client;

import api.models.Credentials;
import api.models.User;
import api.specs.ApiSpecs;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserApiClient {

    private final String baseURI;
    private final static String REGISTER_USER = "/api/auth/register";
    private final static String LOGIN_USER = "/api/auth/login";
    private final static String USER = "/api/auth/user";

    public UserApiClient(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return ApiSpecs.requestSpec(baseURI)
                .body(user)
                .post(REGISTER_USER)
                .then()
                .log().body();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(Credentials credentials) {
        return ApiSpecs.requestSpec(baseURI)
                .body(credentials)
                .post(LOGIN_USER)
                .then()
                .log().body();
    }

    @Step("Обновление пользователя")
    public ValidatableResponse updateUser(User user, String accessToken) {
        return ApiSpecs.requestSpec(baseURI)
                .header("Authorization", accessToken)
                .body(user)
                .patch(USER)
                .then()
                .log().body();
    }

    @Step("Обновление пользователя без токена авторизации")
    public ValidatableResponse updateUserWithoutAuthorizationToken(User user) {
        return ApiSpecs.requestSpec(baseURI)
                .body(user)
                .patch(USER)
                .then()
                .log().body();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return ApiSpecs.requestSpec(baseURI)
                .header("Authorization", accessToken)
                .delete(USER)
                .then()
                .log().body();
    }
}
