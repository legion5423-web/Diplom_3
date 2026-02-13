package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.ConstructorPage;
import util.DriverFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

@RunWith(Parameterized.class)
public class ConstructorTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage mainPage;
    private ConstructorPage constructorPage;

    private String tabToClick;
    private String expectedTabText;

    public ConstructorTests(String tabToClick, String expectedTabText) {
        this.tabToClick = tabToClick;
        this.expectedTabText = expectedTabText;
    }

    @Parameterized.Parameters(name = "Вкладка: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"buns", "Булки"},
                {"sauces", "Соусы"},
                {"fillings", "Начинки"}
        });
    }

    @Before
    @DisplayName("Настройка тестового окружения")
    @Description("Инициализация драйвера, открытие главной страницы, принятие куки")
    public void setUp() {
        driver = DriverFactory.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        mainPage = new MainPage(driver);
        constructorPage = new ConstructorPage(driver);

        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();

        // Прокручиваем к конструктору
        scrollToConstructor();
    }

    @Step("Прокрутить к конструктору")
    private void scrollToConstructor() {
        // Используем JavaScript для прокрутки к началу конструктора
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 400);");

        // Ждем немного для анимации
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Проверка переключения вкладок конструктора")
    @Description("Тестирование переключения между вкладками: Булки, Соусы, Начинки")
    public void testConstructorTabs() {
        clickTab(tabToClick);
        checkTabIsActive(expectedTabText);
        verifySectionIsVisible(tabToClick);
    }

    @Step("Клик по вкладке: {tabName}")
    private void clickTab(String tabName) {
        switch (tabName) {
            case "buns":
                constructorPage.clickBunsTab();
                break;
            case "sauces":
                constructorPage.clickSaucesTab();
                break;
            case "fillings":
                constructorPage.clickFillingsTab();
                break;
        }
    }

    @Step("Проверка, что активна вкладка: {expectedTab}")
    private void checkTabIsActive(String expectedTab) {
        // Ждем обновления активной вкладки
        wait.until(d -> {
            String activeTab = constructorPage.getActiveTabText();
            return activeTab.contains(expectedTab);
        });

        String activeTab = constructorPage.getActiveTabText();
        assertEquals("Активная вкладка должна быть: " + expectedTab,
                expectedTab, activeTab);
    }

    @Step("Проверка видимости секции: {section}")
    private void verifySectionIsVisible(String section) {
        switch (section) {
            case "buns":
                wait.until(d -> constructorPage.isBunsSectionVisible());
                assertTrue("Должна отображаться секция булок",
                        constructorPage.isBunsSectionVisible());
                break;
            case "sauces":
                wait.until(d -> constructorPage.isSaucesSectionVisible());
                assertTrue("Должна отображаться секция соусов",
                        constructorPage.isSaucesSectionVisible());
                break;
            case "fillings":
                wait.until(d -> constructorPage.isFillingsSectionVisible());
                assertTrue("Должна отображаться секция начинок",
                        constructorPage.isFillingsSectionVisible());
                break;
        }
    }

    @After
    @DisplayName("Завершение теста")
    @Description("Закрытие браузера после выполнения теста")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}