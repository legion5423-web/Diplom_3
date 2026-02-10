package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.qameta.allure.Step;

public class MainPage extends BasePage {

    // Локаторы для основной структуры
    private By header = By.className("AppHeader_header__logo__2D0X2");
    private By constructorButton = By.xpath("//p[text()='Конструктор']");
    private By logoButton = By.xpath("//div[contains(@class, 'AppHeader_header__logo')]");
    private By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");

    // Локаторы для кнопок входа и регистрации
    private By loginToAccountButton = By.xpath("//button[text()='Войти в аккаунт']");

    // Локаторы для Cookie
    private By cookieButton = By.xpath("//button[contains(text(), 'да все привыкли')]");

    // Локаторы для кнопки "Оформить заказ" (если она есть на главной)
    private By orderButton = By.xpath("//button[contains(text(), 'Оформить заказ')]");

    // Локатор для сообщения о необходимости авторизации
    private By authRequiredMessage = By.xpath("//p[contains(text(), 'войти в аккаунт')]");

    // Конструктор с вызовом конструктора родительского класса
    public MainPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть главную страницу Stellar Burgers")
    public void open() {
        driver.get("https://stellarburgers.education-services.ru/");
    }

    @Step("Дождаться загрузки главной страницы")
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
        wait.until(ExpectedConditions.elementToBeClickable(loginToAccountButton));
    }

    @Step("Принять cookies, если появилось окно")
    public void acceptCookies() {
        try {
            WebElement button = driver.findElement(cookieButton);
            if (button.isDisplayed()) {
                button.click();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieButton));
            }
        } catch (Exception e) {
            // Окно с cookies не появилось, продолжаем
        }
    }

    @Step("Нажать кнопку 'Войти в аккаунт'")
    public void clickLoginToAccountButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginToAccountButton));
        button.click();
    }

    @Step("Нажать кнопку 'Личный кабинет'")
    public void clickPersonalAccountButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton));
        button.click();
    }

    @Step("Нажать кнопку 'Конструктор'")
    public void clickConstructorButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(constructorButton));
        button.click();
    }

    @Step("Нажать на логотип")
    public void clickLogo() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(logoButton));
        button.click();
    }

    @Step("Проверить, отображается ли кнопка 'Войти в аккаунт'")
    public boolean isLoginButtonDisplayed() {
        try {
            WebElement button = driver.findElement(loginToAccountButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверить, отображается ли кнопка 'Личный кабинет'")
    public boolean isPersonalAccountButtonDisplayed() {
        try {
            WebElement button = driver.findElement(personalAccountButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверить, отображается ли кнопка 'Конструктор'")
    public boolean isConstructorButtonDisplayed() {
        try {
            WebElement button = driver.findElement(constructorButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверить, авторизован ли пользователь")
    public boolean isUserLoggedIn() {
        try {
            WebElement orderButtonElement = driver.findElement(orderButton);
            return orderButtonElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Нажать кнопку 'Оформить заказ'")
    public void clickOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        button.click();
    }

    @Step("Проверить, появилось ли сообщение о необходимости авторизации")
    public boolean isAuthRequiredMessageDisplayed() {
        try {
            WebElement message = driver.findElement(authRequiredMessage);
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Получить текущий URL страницы")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Обновить страницу")
    public void refresh() {
        driver.navigate().refresh();
        waitForLoad();
    }

    @Step("Перейти на главную страницу через логотип")
    public void navigateToHome() {
        clickLogo();
        waitForLoad();
    }
}