package tests;

import api.UserApiClient;
import api.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.LoginPage;
import pages.RegisterPage;
import util.DriverFactory;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Регистрация")
@Feature("Создание аккаунта")
@RunWith(Parameterized.class)
public class RegisterTests {

    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    // Для удаления созданного пользователя
    private UserApiClient userApiClient;
    private String createdEmail;
    private String createdPassword;

    private String password;
    private boolean shouldPass;

    public RegisterTests(String password, boolean shouldPass) {
        this.password = password;
        this.shouldPass = shouldPass;
    }

    @Parameterized.Parameters(name = "Пароль: {0}, Ожидается успех: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"123456", true},        // Минимальная длина - должен пройти
                {"12345", false},        // Слишком короткий - ошибка
                {"password123", true},   // Нормальный пароль
                {"StrongPass!2024", true} // Сложный пароль
        });
    }

    @Before
    @DisplayName("Подготовка к тесту регистрации")
    @Description("Настройка тестового окружения и переход на страницу регистрации")
    public void setUp() {
        driver = DriverFactory.createDriver();
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);

        // Инициализируем API клиент для удаления пользователя
        userApiClient = new UserApiClient();

        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();

        // Переходим на страницу регистрации через главную страницу
        mainPage.clickLoginToAccountButton();
        loginPage.waitForLoad();
        loginPage.clickRegisterLink();

        // Ждем загрузки страницы регистрации
        registerPage.waitForLoad();
    }

    @Test
    @DisplayName("Тестирование регистрации с различными паролями")
    @Description("Проверка регистрации с корректными и некорректными паролями")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Валидация пароля при регистрации")
    public void testRegistration() {
        String name = generateTestName();
        String email = generateTestEmail();

        // Сохраняем данные созданного пользователя для последующего удаления
        if (shouldPass) {
            this.createdEmail = email;
            this.createdPassword = password;
        }

        registerPage.register(name, email, password);

        if (shouldPass) {
            verifySuccessfulRegistration();
        } else {
            verifyRegistrationError();
        }
    }

    @Step("Генерация уникального имени пользователя")
    private String generateTestName() {
        return "TestUser" + System.currentTimeMillis();
    }

    @Step("Генерация уникального email")
    private String generateTestEmail() {
        return "test" + System.currentTimeMillis() + "@example.com";
    }

    @Step("Проверка успешной регистрации")
    private void verifySuccessfulRegistration() {
        // Используем метод из RegisterPage для ожидания редиректа
        registerPage.waitForRedirectToLogin();

        // Проверяем, что перешли на страницу логина
        boolean isOnLoginPage = registerPage.isUrlContains("login");
        assertTrue("Должна быть успешная регистрация и редирект на страницу логина. Текущий URL: " + driver.getCurrentUrl(),
                isOnLoginPage);
    }

    @Step("Проверка ошибки при регистрации")
    private void verifyRegistrationError() {
        // Используем метод из RegisterPage для ожидания ошибки
        registerPage.waitForPasswordError();

        // Проверяем, что ошибка отображается
        boolean isErrorDisplayed = registerPage.isPasswordErrorDisplayed();
        assertTrue("Должна появиться ошибка пароля. Пароль: " + password,
                isErrorDisplayed);

        // При ошибке регистрации пользователь не создается, поэтому не нужно его удалять
        createdEmail = null;
    }

    @After
    @DisplayName("Завершение теста регистрации")
    @Description("Удаление созданного пользователя через API и закрытие браузера")
    public void tearDown() {
        // 1. Удаляем созданного пользователя через API (только если регистрация была успешной)
        if (shouldPass && createdEmail != null && createdPassword != null) {
            try {
                // Создаем временного клиента для удаления пользователя
                UserApiClient cleanupClient = new UserApiClient();

                // Входим под созданным пользователем, чтобы получить токен
                // Для этого нам нужно создать объект User с email и password
                // и использовать метод loginUser() который мы добавим в UserApiClient
                cleanupClient.loginWithCredentials(createdEmail, createdPassword);

                // Удаляем пользователя
                cleanupClient.deleteUser();
                System.out.println("Пользователь удален после теста: " + createdEmail);
            } catch (Exception e) {
                System.err.println("Не удалось удалить пользователя: " + createdEmail);
                e.printStackTrace();
            }
        }

        // 2. Закрываем браузер
        if (driver != null) {
            driver.quit();
        }
    }
}