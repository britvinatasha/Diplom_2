package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderAssertions {

    @Step("Проверка успешного создания заказа")
    public void assertOrderCreated(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Проверка ошибки при создании заказа без ингредиентов")
    public void assertOrderWithoutIngredients(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка успешного получения заказов пользователя")
    public void assertUserOrdersFetched(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("orders", notNullValue());
    }

    @Step("Проверка ошибки при получении заказов неавторизованного пользователя")
    public void assertUserOrdersUnauthorized(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }
@Step("Проверка ошибки при создании заказа с неверным хешем ингредиентов")
    public void assertOrderCreationWithInvalidIngredients(Response response) {
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .assertThat();
    }

}
