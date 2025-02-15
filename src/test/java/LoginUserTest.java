import base.BaseTest;
import user.LoginUser;
import user.CreateUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest extends BaseTest {

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
    @DisplayName("Успешная авторизация пользователя")
    @Description("Проверка, что можно авторизоваться с валидными учетными данными")
    public void testLoginWithValidData() {
        LoginUser loginUser = new LoginUser(email, password);
        Response response = userClient.loginUser(loginUser);
        userAssertions.assertUserLoggedIn(response);
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    @Description("Проверка, что невозможно авторизоваться с неверным email")
    public void testLoginWithInvalidEmail() {
        LoginUser loginUser = new LoginUser("wrong" + email, password);
        Response response = userClient.loginUser(loginUser);
        userAssertions.assertInvalidLoginData(response);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка, что невозможно авторизоваться с неверным паролем")
    public void testLoginWithInvalidPassword() {
        LoginUser loginUser = new LoginUser(email, "wrongPassword");
        Response response = userClient.loginUser(loginUser);
        userAssertions.assertInvalidLoginData(response);
    }

    @Test
    @DisplayName("Авторизация без email")
    @Description("Проверка, что невозможно авторизоваться без email")
    public void testLoginWithoutEmail() {
        LoginUser loginUser = new LoginUser("", password);
        Response response = userClient.loginUser(loginUser);
        userAssertions.assertInvalidLoginData(response);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка, что невозможно авторизоваться без пароля")
    public void testLoginWithoutPassword() {
        LoginUser loginUser = new LoginUser(email, "");
        Response response = userClient.loginUser(loginUser);
        userAssertions.assertInvalidLoginData(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            deleteTestUser(accessToken);
        }
    }
}
