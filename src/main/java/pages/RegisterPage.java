package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        super(driver); // Вызов конструктора BasePage
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(registerHeader));
    }

    public void enterName(String name) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(nameField));
        element.clear();
        element.sendKeys(name);
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

    public void clickRegisterButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        element.click();
    }

    public void register(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        clickRegisterButton();
    }

    public void clickLoginLink() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        element.click();
    }

    public boolean isPasswordErrorDisplayed() {
        try {
            WebElement element = driver.findElement(passwordError);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPasswordErrorText() {
        try {
            WebElement element = driver.findElement(passwordError);
            return element.getText();
        } catch (Exception e) {
            return "";
        }
    }
}