package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ConstructorPage extends BasePage {

    // Локаторы для разделов конструктора
    private By bunsTab = By.xpath("//span[text()='Булки']/parent::div");
    private By saucesTab = By.xpath("//span[text()='Соусы']/parent::div");
    private By fillingsTab = By.xpath("//span[text()='Начинки']/parent::div");
    private By activeTab = By.xpath("//div[contains(@class, 'tab_tab__1SPyG') and contains(@class, 'tab_tab_type_current__2BEPc')]");

    // Локаторы для секций
    private By bunsSection = By.xpath("//h2[text()='Булки']");
    private By saucesSection = By.xpath("//h2[text()='Соусы']");
    private By fillingsSection = By.xpath("//h2[text()='Начинки']");

    // Локаторы для кнопок входа
    private By loginToAccountButton = By.xpath("//button[text()='Войти в аккаунт']");
    private By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");

    public ConstructorPage(WebDriver driver) {
        super(driver); // Вызов конструктора BasePage
    }

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(bunsSection));
    }

    public void clickBunsTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(bunsTab));
        element.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(bunsSection));
    }

    public void clickSaucesTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(saucesTab));
        element.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(saucesSection));
    }

    public void clickFillingsTab() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(fillingsTab));
        element.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(fillingsSection));
    }

    public void clickLoginToAccountButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginToAccountButton));
        element.click();
    }

    public void clickPersonalAccountButton() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton));
        element.click();
    }

    public String getActiveTabText() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        return element.getText();
    }

    public boolean isBunsSectionVisible() {
        try {
            WebElement element = driver.findElement(bunsSection);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSaucesSectionVisible() {
        try {
            WebElement element = driver.findElement(saucesSection);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFillingsSectionVisible() {
        try {
            WebElement element = driver.findElement(fillingsSection);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
