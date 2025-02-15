import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.CreateUser;

public class UpdateUserTest extends BaseTest {

    String email = faker.internet().emailAddress().toLowerCase();
    String password = faker.internet().password();
    String name = faker.name().name();
    String accessToken;

    @Before
    public void setUp() {

        CreateUser user = new CreateUser(email, password, name);
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Успешное обновление данных пользователя")
    @Description("Проверка, что можно обновить данные пользователя с авторизацией")
    public void testUpdateUserWithAuth() {
        CreateUser updatedUser = new CreateUser(faker.internet().emailAddress().toLowerCase(), password, faker.name().firstName());
        Response response = userClient.updateUserInfo(accessToken, updatedUser);
        userAssertions.assertUserUpdated(response);
    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации")
    @Description("Проверка, что нельзя обновить данные пользователя без токена")
    public void testUpdateUserWithoutAuth() {
        CreateUser updatedUser = new CreateUser(faker.internet().emailAddress().toLowerCase(), password, faker.name().firstName());
        Response response = userClient.updateUserInfo("", updatedUser);
        userAssertions.assertUserUpdateUnauthorized(response);
    }

    @Test
    @DisplayName("Обновление email на уже существующий")
    @Description("Проверка, что нельзя обновить email на уже зарегистрированный")
    public void testUpdateUserWithExistingEmail() {

        CreateUser anotherUser = new CreateUser(faker.internet().emailAddress().toLowerCase(), password, faker.name().firstName());
        Response anotherUserResponse = userClient.createUser(anotherUser);
        userAssertions.assertUserCreated(anotherUserResponse);


        CreateUser updatedUser = new CreateUser(anotherUser.getEmail(), password, name);
        Response response = userClient.updateUserInfo(accessToken, updatedUser);
        userAssertions.assertEmailAlreadyExists(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            deleteTestUser(accessToken);
        }
    }
}