package user;

import base.BaseHttpClient;
import base.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseHttpClient {

    @Step("Создание пользователя")
    public Response createUser(CreateUser user) {
        return requestSpecification
                .body(user)
                .when()
                .post(Constants.CREATE_USER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(LoginUser user) {
         return requestSpecification
                .body(user)
                .when()
                .post(Constants.LOGIN_USER);

    }

    @Step("Получение информации о пользователе")
    public Response getUserInfo(String accessToken) {
        return requestSpecification
                .header("Authorization", accessToken)
                .when()
                .get(Constants.EDIT_USER);
    }

    @Step("Обновление информации о пользователе")
    public Response updateUserInfo(String accessToken, CreateUser updatedUser) {
        return requestSpecification
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch(Constants.EDIT_USER);
    }



    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        requestSpecification
                .header("Authorization", accessToken)
                .when()
                .delete(Constants.DELETE_USER);
    }

}
