package tests;

import api.UserApiClient;
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

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

@Epic("Авторизация")
@Feature("Вход в систему")
@RunWith(Parameterized.class)
public class LoginTests {

    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private UserApiClient userApiClient;

    private String loginMethod; // "main", "personal", "register", "recover"

    public LoginTests(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    @Parameterized.Parameters(name = "Метод входа: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"main"},
                {"personal"},
                {"register"},
                {"recover"}
        });
    }

    @Before
    @DisplayName("Подготовка к тесту входа")
    @Description("Инициализация драйвера, создание пользователя через API, открытие главной страницы")
    public void setUp() {
        // 1. Создаем случайного пользователя через API
        userApiClient = new UserApiClient();
        userApiClient.createUser();
        System.out.println("Создан пользователь для теста: " + userApiClient.getEmail());

        // 2. Инициализируем драйвер
        driver = DriverFactory.createDriver();
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);

        // 3. Открываем главную страницу
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();
    }

    @Test
    @DisplayName("Тестирование входа в систему через различные методы")
    @Description("Проверка возможности входа через: главную кнопку, личный кабинет, форму регистрации, форму восстановления пароля")
    @Story("Различные способы входа в аккаунт")
    public void testLogin() {
        // 1. Переходим на страницу входа выбранным методом
        navigateToLoginPage(loginMethod);

        // 2. Ждем загрузки страницы логина
        loginPage.waitForLoad();

        // 3. Вводим логин и пароль созданного через API пользователя
        String email = userApiClient.getEmail();
        String password = userApiClient.getPassword();

        loginPage.login(email, password);
        System.out.println("Выполнен вход с email: " + email);

        // 4. Ждем успешного входа (используем метод из MainPage)
        mainPage.waitForSuccessfulLogin();

        // 5. Проверяем, что вход выполнен успешно
        boolean isLoggedIn = mainPage.isUserLoggedIn() ||
                !mainPage.isUrlContains("login") ||
                mainPage.isOrderButtonDisplayed();

        assertTrue("Вход должен быть выполнен успешно для метода: " + loginMethod, isLoggedIn);
    }

    @Step("Переход на страницу входа через метод: {method}")
    private void navigateToLoginPage(String method) {
        switch (method) {
            case "main":
                mainPage.clickLoginToAccountButton();
                break;
            case "personal":
                mainPage.clickPersonalAccountButton();
                break;
            case "register":
                mainPage.clickLoginToAccountButton();
                loginPage.waitForLoad();
                loginPage.clickRegisterLink();
                registerPage.waitForLoad();
                registerPage.clickLoginLink();
                break;
            case "recover":
                mainPage.clickLoginToAccountButton();
                loginPage.waitForLoad();
                loginPage.clickRecoverPasswordLink();
                // Ждем загрузки страницы восстановления пароля
                loginPage.waitForForgotPasswordPage();
                mainPage.goBack(); // Возвращаемся на страницу входа
                break;
        }
    }

    @After
    @DisplayName("Завершение теста")
    @Description("Удаление пользователя через API и закрытие браузера")
    public void tearDown() {
        // 1. Удаляем созданного пользователя через API
        if (userApiClient != null) {
            userApiClient.deleteUser();
            System.out.println("Пользователь удален после теста");
        }

        // 2. Закрываем браузер
        if (driver != null) {
            driver.quit();
        }
    }
}