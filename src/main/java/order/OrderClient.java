package order;
import base.BaseHttpClient;
import base.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseHttpClient {

    @Step("Создание заказа")
    public Response createOrder(CreateOrder order, String accessToken) {
        return requestSpecification
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(Constants.CREATE_ORDER);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken) {
        return requestSpecification
                .header("Authorization", accessToken)
                .when()
                .get(Constants.GET_ORDER);
    }
}
