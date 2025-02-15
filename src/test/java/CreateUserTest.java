import base.BaseTest;
import user.CreateUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


public class CreateUserTest extends BaseTest {

    String email = faker.internet().emailAddress().toLowerCase();
    String password = faker.internet().password();
    String name = faker.name().name();
    String accessToken;

@Test
@DisplayName("Создание пользователя с валидными данными")
@Description("Проверка, что можно создать пользователя с валидными данными")
public void testCreateUserWithValidData() {
    CreateUser user = new CreateUser(email, password,name);
    Response response = userClient.createUser(user);
    accessToken = response.path("accessToken");
    userAssertions.assertUserCreated(response);
}

@Test
@DisplayName("Создание пользователя без email")
@Description ("Проверка, что невозможно создать пользователя с пустым полем email")
    public void testCreateUserWithEmptyEmail() {
    CreateUser user = new CreateUser("", password,name);
    Response response = userClient.createUser(user);
    userAssertions.assertUserCreationWithoutRequiredField(response);
}

@Test
@DisplayName("Создание пользователя без пароля")
@Description("Проверка, что невозможно создать пользователя с пустым полем password")
public void testCreateUserWithEmptyPassword() {
    CreateUser user = new CreateUser(email, "", name);
    Response response = userClient.createUser(user);
    userAssertions.assertUserCreationWithoutRequiredField(response);
    }

@Test
@DisplayName("Создание пользователя без имени")
@Description("Проверка, что невозможно создать пользователя с пустым полем name")
    public void testCreateUserWithEmptyName() {
        CreateUser user = new CreateUser(email, password, "");
        Response response = userClient.createUser(user);
        userAssertions.assertUserCreationWithoutRequiredField(response);
    }

@Test
@DisplayName("Создание двух одинаковых пользователей")
@Description("Проверка, что невозможно создать двух одинаковых пользователей")
    public void testCreateDuplicateUser() {
    CreateUser user = new CreateUser(email, password, name);
    Response responseFirst = userClient.createUser(user);
    userAssertions.assertUserCreated(responseFirst);

    Response responseSecond = userClient.createUser(user);
    userAssertions.assertUserAlreadyExists(responseSecond);
    }

@After
    public void cleanUp() {
    if (accessToken != null) {
        deleteTestUser(accessToken);
        }

    }
}


