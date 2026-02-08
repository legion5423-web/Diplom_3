package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.ConstructorPage;
import pages.RegisterPage;
import util.DriverFactory;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

// Импорты для Allure
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
    private ConstructorPage constructorPage;
    private RegisterPage registerPage;

    private String browser;
    private String password;
    private boolean shouldPass;

    public RegisterTests(String browser, String password, boolean shouldPass) {
        this.browser = browser;
        this.password = password;
        this.shouldPass = shouldPass;
    }

    @Parameterized.Parameters(name = "Браузер: {0}, Пароль: {1}, Ожидается: {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"chrome", "123456", true},        // Минимальная длина - должен пройти
                {"chrome", "12345", false},        // Слишком короткий - ошибка
                {"chrome", "password123", true},   // Нормальный пароль
                {"yandex", "123456", true},
                {"yandex", "123", false},
                {"yandex", "strongpass", true}
        });
    }

    @Before
    @DisplayName("Подготовка к тесту регистрации")
    @Description("Настройка тестового окружения и переход на страницу регистрации")
    public void setUp() {
        driver = DriverFactory.createDriver(browser);
        mainPage = new MainPage(driver);
        constructorPage = new ConstructorPage(driver);
        registerPage = new RegisterPage(driver);

        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();

        // Переходим на страницу регистрации
        constructorPage.clickLoginToAccountButton();
        driver.get("https://stellarburgers.education-services.ru/register");
    }

    @Test
    @DisplayName("Тестирование регистрации с различными паролями")
    @Description("Проверка регистрации с корректными и некорректными паролями")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Валидация пароля при регистрации")
    public void testRegistration() throws InterruptedException {
        String name = generateTestName();
        String email = generateTestEmail();

        registerPage.waitForLoad();
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
    private void verifySuccessfulRegistration() throws InterruptedException {
        Thread.sleep(2000); // Ждем редирект
        boolean isNotOnRegisterPage = !driver.getCurrentUrl().contains("/register");
        assertTrue("Должна быть успешная регистрация. Текущий URL: " + driver.getCurrentUrl(),
                isNotOnRegisterPage);
    }

    @Step("Проверка ошибки при регистрации")
    private void verifyRegistrationError() throws InterruptedException {
        Thread.sleep(1000);
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
