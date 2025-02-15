package base;

import com.github.javafaker.Faker;
import order.OrderAssertions;
import user.UserAssertions;
import user.UserClient;
import order.OrderClient;


public class BaseTest {

    protected final Faker faker = new Faker();
    protected final UserClient userClient = new UserClient();
    protected final OrderClient orderClient = new OrderClient();
    protected final UserAssertions userAssertions = new UserAssertions();
    protected final OrderAssertions orderAssertions = new OrderAssertions();


    protected void deleteTestUser(String accessToken) {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
