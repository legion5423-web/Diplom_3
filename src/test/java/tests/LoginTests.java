package tests;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.ConstructorPage;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;
import util.DriverFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@Epic("Авторизация")
@Feature("Вход в систему")
@RunWith(Parameterized.class)
public class LoginTests {

    private WebDriver driver;
    private MainPage mainPage;
    private ConstructorPage constructorPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    private String browser;
    private String loginMethod; // "main", "personal", "register", "recover"

    public LoginTests(String browser, String loginMethod) {
        this.browser = browser;
        this.loginMethod = loginMethod;
    }

    @Parameterized.Parameters(name = "Браузер: {0}, Метод входа: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"chrome", "main"},
                {"chrome", "personal"},
                {"chrome", "register"},
                {"chrome", "recover"},
                {"yandex", "main"},
                {"yandex", "personal"},
                {"yandex", "register"},
                {"yandex", "recover"}
        });
    }

    @Before
    @DisplayName("Подготовка к тесту входа")
    @Description("Инициализация драйвера и открытие главной страницы")
    public void setUp() {
        driver = DriverFactory.createDriver(browser);
        mainPage = new MainPage(driver);
        constructorPage = new ConstructorPage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);

        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();
    }

    @Test
    @DisplayName("Тестирование входа в систему через различные методы")
    @Description("Проверка возможности входа через: главную кнопку, личный кабинет, форму регистрации, форму восстановления пароля")
    @Story("Различные способы входа в аккаунт")
    public void testLogin() throws InterruptedException {
        navigateToLoginPage(loginMethod);
        performLogin("legion5423@gmail.com", "J7Y-Qct-kUR-kW6");
        verifySuccessfulLogin();
    }

    @Step("Переход на страницу входа через метод: {method}")
    private void navigateToLoginPage(String method) {
        switch (method) {
            case "main":
                constructorPage.clickLoginToAccountButton();
                break;
            case "personal":
                constructorPage.clickPersonalAccountButton();
                break;
            case "register":
                constructorPage.clickLoginToAccountButton();
                loginPage.waitForLoad();
                loginPage.clickRegisterLink();
                registerPage.waitForLoad();
                registerPage.clickLoginLink();
                break;
            case "recover":
                constructorPage.clickLoginToAccountButton();
                loginPage.waitForLoad();
                loginPage.clickRecoverPasswordLink();
                driver.navigate().back(); // Возвращаемся на страницу входа
                break;
        }
    }

    @Step("Выполнение входа с email: {email}")
    private void performLogin(String email, String password) {
        loginPage.waitForLoad();
        loginPage.login(email, password);
    }

    @Step("Проверка успешного входа")
    private void verifySuccessfulLogin() throws InterruptedException {
        Thread.sleep(2000); // Ждем редирект
        assertTrue("Должен произойти вход в систему. Текущий URL: " + driver.getCurrentUrl(),
                !driver.getCurrentUrl().contains("/login"));
    }

    @After
    @DisplayName("Завершение теста")
    @Description("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}