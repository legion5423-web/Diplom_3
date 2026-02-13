package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.qameta.allure.Step;

public class RegisterPage extends BasePage {

    // Локаторы
    private By nameField = By.xpath("//fieldset[1]//input[@name='name']");
    private By emailField = By.xpath("//fieldset[2]//input[@name='name']");
    private By passwordField = By.xpath("//input[@name='Пароль']");
    private By registerButton = By.xpath("//button[text()='Зарегистрироваться']");
    private By loginLink = By.xpath("//a[text()='Войти']");
    private By registerHeader = By.xpath("//h2[text()='Регистрация']");
    private By passwordError = By.xpath("//p[contains(text(),'Некорректный пароль')]");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    @Step("Дождаться загрузки страницы регистрации")
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(registerHeader));
    }

    @Step("Ввести имя: {name}")
    public void enterName(String name) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(nameField));
        element.clear();
        element.sendKeys(name);
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

    @Step("Нажать кнопку 'Зарегистрироваться'")
    public void clickRegisterButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        element.click();
    }

    @Step("Выполнить регистрацию с именем: {name}")
    public void register(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        clickRegisterButton();
    }

    @Step("Нажать ссылку 'Войти'")
    public void clickLoginLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        element.click();
    }

    @Step("Проверить, отображается ли ошибка пароля")
    public boolean isPasswordErrorDisplayed() {
        try {
            WebElement element = driver.findElement(passwordError);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Дождаться появления ошибки пароля")
    public void waitForPasswordError() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordError));
    }

    @Step("Получить текст ошибки пароля")
    public String getPasswordErrorText() {
        try {
            WebElement element = driver.findElement(passwordError);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Дождаться редиректа на страницу логина")
    public void waitForRedirectToLogin() {
        wait.until(ExpectedConditions.urlContains("login"));
    }

    @Step("Проверить, что текущий URL содержит текст: {text}")
    public boolean isUrlContains(String text) {
        return driver.getCurrentUrl().contains(text);
    }
}