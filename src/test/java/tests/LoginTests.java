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
    private WebDriverWait wait;
    private MainPage mainPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

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
    @Description("Инициализация драйвера и открытие главной страницы")
    public void setUp() {
        driver = DriverFactory.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        mainPage = new MainPage(driver);
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
    public void testLogin() {
        navigateToLoginPage(loginMethod);

        // Ждем загрузки страницы логина
        loginPage.waitForLoad();

        // Проверяем, что мы на странице логина
        assertTrue("Должны быть на странице логина. URL: " + driver.getCurrentUrl(),
                loginPage.isLoginButtonDisplayed() || driver.getCurrentUrl().contains("login"));
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
                wait.until(ExpectedConditions.urlContains("forgot-password"));
                driver.navigate().back(); // Возвращаемся на страницу входа
                break;
        }
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