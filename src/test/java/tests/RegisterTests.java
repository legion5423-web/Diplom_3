package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.LoginPage;
import pages.RegisterPage;
import util.DriverFactory;
import java.time.Duration;
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
    private WebDriverWait wait;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);

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
        // Ждем редиректа на страницу логина после успешной регистрации
        wait.until(ExpectedConditions.urlContains("login"));

        boolean isOnLoginPage = driver.getCurrentUrl().contains("login");
        assertTrue("Должна быть успешная регистрация и редирект на страницу логина. Текущий URL: " + driver.getCurrentUrl(),
                isOnLoginPage);
    }

    @Step("Проверка ошибки при регистрации")
    private void verifyRegistrationError() {
        // Ждем появления ошибки пароля
        wait.until(d -> registerPage.isPasswordErrorDisplayed());

        boolean isErrorDisplayed = registerPage.isPasswordErrorDisplayed();
        assertTrue("Должна появиться ошибка пароля. Пароль: " + password,
                isErrorDisplayed);
    }

    @After
    @DisplayName("Завершение теста регистрации")
    @Description("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}