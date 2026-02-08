package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        super(driver); // Вызов конструктора BasePage
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginHeader));
    }

    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(emailField));
        element.clear();
        element.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        element.clear();
        element.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        element.click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public void clickRegisterLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(registerLink));
        element.click();
    }

    public void clickRecoverPasswordLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(recoverPasswordLink));
        element.click();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebElement element = driver.findElement(errorMessage);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement element = driver.findElement(errorMessage);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }
}