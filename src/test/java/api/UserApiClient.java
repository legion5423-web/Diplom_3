package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserApiClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    private String email;
    private String password;
    private String name;
    private String accessToken;

    public UserApiClient() {
        // Генерируем уникальные данные для пользователя
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        this.email = "testuser_" + uuid + "_" + timestamp + "@example.com";
        this.password = "Password123!";
        this.name = "TestUser_" + uuid;
    }

    @Step("Создать нового пользователя через API")
    public void createUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("name", name);

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(userData)
                .when()
                .post("auth/register");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
            if (accessToken != null) {
                // Удаляем "Bearer " префикс если он есть
                accessToken = accessToken.replace("Bearer ", "");
            }
            System.out.println("Пользователь создан: " + email);
        } else {
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
                System.out.println("Пользователь удален: " + email);
            } else {
                System.out.println("Не удалось удалить пользователя. Код ответа: " + response.getStatusCode());
            }
        }
    }

    @Step("Войти пользователем через API и получить токен")
    public void loginUser() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(loginData)
                .when()
                .post("auth/login");

        if (response.getStatusCode() == 200) {
            accessToken = response.jsonPath().getString("accessToken");
            if (accessToken != null) {
                accessToken = accessToken.replace("Bearer ", "");
            }
            System.out.println("Пользователь вошел: " + email);
        } else {
            throw new RuntimeException("Не удалось войти пользователем. Код ответа: " + response.getStatusCode());
        }
    }

    // Геттеры
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }
}