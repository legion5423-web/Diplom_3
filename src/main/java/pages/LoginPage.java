package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    // Локаторы
    private By emailField = By.xpath("//input[@name='name']");
    private By passwordField = By.xpath("//input[@name='Пароль']");
    private By loginButton = By.xpath("//button[text()='Войти']");
    private By registerLink = By.xpath("//a[text()='Зарегистрироваться']");
    private By recoverPasswordLink = By.xpath("//a[text()='Восстановить пароль']");
    private By loginHeader = By.xpath("//h2[text()='Вход']");
    private By errorMessage = By.xpath("//p[@class='input__error text_type_main-default']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться загрузки страницы входа")
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeader));
    }

    @Step("Ввести email: {email}")
    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(emailField));
        element.clear();
        element.sendKeys(email);
    }

    @Step("Ввести пароль")
    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        element.clear();
        element.sendKeys(password);
    }

    @Step("Нажать кнопку 'Войти'")
    public void clickLoginButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        element.click();
    }

    @Step("Выполнить вход с email: {email}")
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    @Step("Нажать ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(registerLink));
        element.click();
    }

    @Step("Нажать ссылку 'Восстановить пароль'")
    public void clickRecoverPasswordLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(recoverPasswordLink));
        element.click();
    }

    @Step("Проверить, отображается ли сообщение об ошибке")
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement element = driver.findElement(errorMessage);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Дождаться появления сообщения об ошибке")
    public void waitForErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
    }

    @Step("Получить текст сообщения об ошибке")
    public String getErrorMessageText() {
        try {
            WebElement element = driver.findElement(errorMessage);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Проверить, отображается ли кнопка 'Войти'")
    public boolean isLoginButtonDisplayed() {
        try {
            WebElement element = driver.findElement(loginButton);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверить, загрузилась ли страница входа")
    public boolean isLoginPageLoaded() {
        try {
            WebElement element = driver.findElement(loginHeader);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Дождаться редиректа на страницу восстановления пароля")
    public void waitForForgotPasswordPage() {
        wait.until(ExpectedConditions.urlContains("forgot-password"));
    }
}