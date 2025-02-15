import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.CreateUser;

public class GetOrderListTest extends BaseTest {

    private String accessToken;

    @Before
    public void setUp() {
        CreateUser user = new CreateUser(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка, что авторизованный пользователь может получить список своих заказов")
    public void testGetOrdersWithAuth() {
        Response response = orderClient.getUserOrders(accessToken);
        orderAssertions.assertUserOrdersFetched(response);
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("Проверка, что нельзя получить заказы без авторизации")
    public void testGetOrdersWithoutAuth() {
        Response response = orderClient.getUserOrders("");
        orderAssertions.assertUserOrdersUnauthorized(response);
    }

    @After
    public void cleanUp() {
        deleteTestUser(accessToken);
    }
}

