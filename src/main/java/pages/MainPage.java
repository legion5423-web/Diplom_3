package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage extends BasePage {

    // Локаторы для основной структуры
    private By header = By.className("AppHeader_header__logo__2D0X2");
    private By constructorButton = By.xpath("//p[text()='Конструктор']");
    private By logoButton = By.xpath("//div[contains(@class, 'AppHeader_header__logo')]");
    private By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");

    // Локаторы для кнопок входа и регистрации
    private By loginToAccountButton = By.xpath("//button[text()='Войти в аккаунт']");

    // Локаторы для конструктора
    private By bunsTab = By.xpath("//span[text()='Булки']/parent::div");
    private By saucesTab = By.xpath("//span[text()='Соусы']/parent::div");
    private By fillingsTab = By.xpath("//span[text()='Начинки']/parent::div");
    private By activeTab = By.xpath("//div[contains(@class, 'tab_tab__1SPyG tab_tab_type_current__2BEPc')]");

    // Локаторы для секций конструктора
    private By bunsSection = By.xpath("//h2[text()='Булки']");
    private By saucesSection = By.xpath("//h2[text()='Соусы']");
    private By fillingsSection = By.xpath("//h2[text()='Начинки']");

    // Локаторы для Cookie
    private By cookieButton = By.xpath("//button[contains(text(), 'да все привыкли')]");

    // Локаторы для кнопки "Оформить заказ" (если она есть на главной)
    private By orderButton = By.xpath("//button[contains(text(), 'Оформить заказ')]");

    // Локатор для сообщения о необходимости авторизации
    private By authRequiredMessage = By.xpath("//p[contains(text(), 'войти в аккаунт')]");

    // Конструктор с вызовом конструктора родительского класса
    public MainPage(WebDriver driver) {
        super(driver); // Важно: передаем driver в конструктор BasePage
    }

    /**
     * Открыть главную страницу Stellar Burgers
     */
    public void open() {
        driver.get("https://stellarburgers.education-services.ru/");
    }

    /**
     * Дождаться загрузки главной страницы
     */
    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
        wait.until(ExpectedConditions.elementToBeClickable(loginToAccountButton));
    }

    /**
     * Принять cookies, если появилось окно
     */
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

    /**
     * Нажать кнопку "Войти в аккаунт"
     */
    public void clickLoginToAccountButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginToAccountButton));
        button.click();
    }

    /**
     * Нажать кнопку "Личный кабинет"
     */
    public void clickPersonalAccountButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton));
        button.click();
    }

    /**
     * Нажать кнопку "Конструктор"
     */
    public void clickConstructorButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(constructorButton));
        button.click();
    }

    /**
     * Нажать на логотип
     */
    public void clickLogo() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(logoButton));
        button.click();
    }

    /**
     * Проверить, отображается ли кнопка "Войти в аккаунт"
     */
    public boolean isLoginButtonDisplayed() {
        try {
            WebElement button = driver.findElement(loginToAccountButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, отображается ли кнопка "Личный кабинет"
     */
    public boolean isPersonalAccountButtonDisplayed() {
        try {
            WebElement button = driver.findElement(personalAccountButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, отображается ли кнопка "Конструктор"
     */
    public boolean isConstructorButtonDisplayed() {
        try {
            WebElement button = driver.findElement(constructorButton);
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Методы для работы с конструктором

    /**
     * Нажать вкладку "Булки"
     */
    public void clickBunsTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(bunsTab));
        scrollToElement(tab);
        tab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(bunsSection));
    }

    /**
     * Нажать вкладку "Соусы"
     */
    public void clickSaucesTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(saucesTab));
        scrollToElement(tab);
        tab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(saucesSection));
    }

    /**
     * Нажать вкладку "Начинки"
     */
    public void clickFillingsTab() {
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(fillingsTab));
        scrollToElement(tab);
        tab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(fillingsSection));
    }

    /**
     * Получить текст активной вкладки
     */
    public String getActiveTabText() {
        WebElement activeTabElement = wait.until(ExpectedConditions.visibilityOfElementLocated(activeTab));
        return activeTabElement.getText();
    }

    /**
     * Проверить, отображается ли секция "Булки"
     */
    public boolean isBunsSectionVisible() {
        try {
            WebElement section = driver.findElement(bunsSection);
            return section.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, отображается ли секция "Соусы"
     */
    public boolean isSaucesSectionVisible() {
        try {
            WebElement section = driver.findElement(saucesSection);
            return section.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, отображается ли секция "Начинки"
     */
    public boolean isFillingsSectionVisible() {
        try {
            WebElement section = driver.findElement(fillingsSection);
            return section.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверить, авторизован ли пользователь
     * (есть ли кнопка "Оформить заказ" вместо "Войти в аккаунт")
     */
    public boolean isUserLoggedIn() {
        try {
            // Если пользователь авторизован, должна быть кнопка "Оформить заказ"
            WebElement orderButtonElement = driver.findElement(orderButton);
            return orderButtonElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Нажать кнопку "Оформить заказ" (только для авторизованных пользователей)
     */
    public void clickOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        button.click();
    }

    /**
     * Проверить, появилось ли сообщение о необходимости авторизации
     */
    public boolean isAuthRequiredMessageDisplayed() {
        try {
            WebElement message = driver.findElement(authRequiredMessage);
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить текущий URL страницы
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Прокрутить страницу к секции конструктора
     */
    public void scrollToConstructor() {
        WebElement bunsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(bunsSection));
        scrollToElement(bunsElement);
    }

    /**
     * Обновить страницу
     */
    public void refresh() {
        driver.navigate().refresh();
        waitForLoad();
    }

    /**
     * Перейти на главную страницу через логотип или конструктор
     */
    public void navigateToHome() {
        clickLogo();
        waitForLoad();
    }
}