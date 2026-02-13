package api;

import api.model.User;
import api.model.UserResponse;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.UUID;

public class UserApiClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    private User user;
    private String accessToken;

    public UserApiClient() {
        // Генерируем уникальные данные для пользователя
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        String email = "testuser_" + uuid + "_" + timestamp + "@example.com";
        String password = "Password123!";
        String name = "TestUser_" + uuid;

        // Создаем объект User через конструктор (сериализация)
        this.user = new User(email, password, name);
    }

    @Step("Создать нового пользователя через API")
    public void createUser() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                // Сериализация: объект user автоматически преобразуется в JSON
                .body(user)
                .when()
                .post("auth/register");

        if (response.getStatusCode() == 200) {
            // Десериализация: JSON ответа преобразуется в объект UserResponse
            UserResponse userResponse = response.as(UserResponse.class);
            accessToken = userResponse.getAccessToken();
            if (accessToken != null) {
                accessToken = accessToken.replace("Bearer ", "");
            }
            System.out.println("Пользователь создан: " + user.getEmail());
        } else {
            System.out.println("Ошибка создания пользователя. Код: " + response.getStatusCode());
            System.out.println("Ответ: " + response.asString());
            throw new RuntimeException("Не удалось создать пользователя. Код ответа: " + response.getStatusCode());
        }
    }

    @Step("Удалить пользователя через API")
    public void deleteUser() {
        if (accessToken != null && !accessToken.isEmpty()) {
            Response response = RestAssured.given()
                    .baseUri(BASE_URL)
                    .header("Authorization", accessToken)
                    .when()
                    .delete("auth/user");

            if (response.getStatusCode() == 202) {
                System.out.println("Пользователь удален: " + user.getEmail());
            } else {
                System.out.println("Не удалось удалить пользователя. Код ответа: " + response.getStatusCode());
                System.out.println("Ответ: " + response.asString());
            }
        }
    }

    @Step("Войти пользователем через API и получить токен")
    public void loginUser() {
        // Создаем объект для логина (только email и password)
        User loginUser = new User(user.getEmail(), user.getPassword());

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                // Сериализация объекта loginUser в JSON
                .body(loginUser)
                .when()
                .post("auth/login");

        if (response.getStatusCode() == 200) {
            // Десериализация ответа
            UserResponse userResponse = response.as(UserResponse.class);
            accessToken = userResponse.getAccessToken();
            if (accessToken != null) {
                accessToken = accessToken.replace("Bearer ", "");
            }
            System.out.println("Пользователь вошел: " + user.getEmail());
        } else {
            System.out.println("Ошибка входа. Код: " + response.getStatusCode());
            System.out.println("Ответ: " + response.asString());
            throw new RuntimeException("Не удалось войти пользователем. Код ответа: " + response.getStatusCode());
        }
    }

    @Step("Войти с указанными учетными данными через API")
    public void loginWithCredentials(String email, String password) {
        // Создаем объект для логина (только email и password)
        User loginUser = new User(email, password);

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                // Сериализация объекта loginUser в JSON
                .body(loginUser)
                .when()
                .post("auth/login");

        if (response.getStatusCode() == 200) {
            // Десериализация ответа
            UserResponse userResponse = response.as(UserResponse.class);
            accessToken = userResponse.getAccessToken();
            if (accessToken != null) {
                accessToken = accessToken.replace("Bearer ", "");
            }
            System.out.println("Вход выполнен для пользователя: " + email);
        } else {
            System.out.println("Ошибка входа. Код: " + response.getStatusCode());
            System.out.println("Ответ: " + response.asString());
            throw new RuntimeException("Не удалось войти пользователем. Код ответа: " + response.getStatusCode());
        }
    }

    // Геттеры
    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getName() {
        return user.getName();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }
}