import base.BaseTest;
import order.CreateOrder;
import order.OrderAssertions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.CreateUser;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest extends BaseTest {

    private OrderAssertions orderAssertions;
    private OrderClient orderClient;

    private String accessToken;
    private final List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f");
    private final List<String> wrongIngredients = List.of("61c0c5a71d1f82001bdaaa6dd");

    @Before
    public void setUp() {

        orderAssertions = new OrderAssertions();
        orderClient = new OrderClient();

        CreateUser user = new CreateUser(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка, что авторизованный пользователь может создать заказ")
    public void testCreateOrderWithAuth() {
        CreateOrder order = new CreateOrder(ingredients);
        Response response = orderClient.createOrder(order, accessToken);
        orderAssertions.assertOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что нельзя создать заказ без авторизации")
    public void testCreateOrderWithoutAuth() {
        CreateOrder order = new CreateOrder(ingredients);
        Response response = orderClient.createOrder(order, "");
        orderAssertions.assertUserOrdersUnauthorized(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка, что нельзя создать заказ без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        CreateOrder order = new CreateOrder(new ArrayList<>());
        Response response = orderClient.createOrder(order, accessToken);
        orderAssertions.assertOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка, что нельзя создать заказ с неверным ID ингредиентов")
    public void testCreateOrderWithInvalidIngredients() {
        CreateOrder order = new CreateOrder(wrongIngredients);
        Response response = orderClient.createOrder(order, accessToken);
        orderAssertions.assertOrderCreationWithInvalidIngredients(response);
    }

    @After
    public void cleanUp() {
        deleteTestUser(accessToken);
    }
}