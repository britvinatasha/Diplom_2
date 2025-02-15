package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserAssertions {

    @Step("Проверка успешного создания пользователя")
    public void assertUserCreated(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Step("Проверка ошибки при создании уже зарегистрированного пользователя")
    public void assertUserAlreadyExists(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка ошибки при создании пользователя без обязательного поля")
    public void assertUserCreationWithoutRequiredField(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка успешного логина пользователя")
    public void assertUserLoggedIn(Response response) {
        response.then()
                .log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка ошибки при логине с неверными данными")
    public void assertInvalidLoginData(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка успешного обновления данных пользователя")
    public void assertUserUpdated(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Step("Проверка ошибки при обновлении данных без авторизации")
    public void assertUserUpdateUnauthorized(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ошибки при изменении email на уже существующий")
    public void assertEmailAlreadyExists(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("message", equalTo("User with such email already exists"));
    }
}
