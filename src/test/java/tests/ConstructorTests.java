package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.ConstructorPage;
import util.DriverFactory;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Импорты для Allure
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

@RunWith(Parameterized.class)
public class ConstructorTests {

    private WebDriver driver;
    private MainPage mainPage;
    private ConstructorPage constructorPage;

    private String browser;
    private String tabToClick; // "buns", "sauces", "fillings"
    private String expectedTabText;

    public ConstructorTests(String browser, String tabToClick, String expectedTabText) {
        this.browser = browser;
        this.tabToClick = tabToClick;
        this.expectedTabText = expectedTabText;
    }

    @Parameterized.Parameters(name = "Браузер: {0}, Вкладка: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"chrome", "buns", "Булки"},
                {"chrome", "sauces", "Соусы"},
                {"chrome", "fillings", "Начинки"},
                {"yandex", "buns", "Булки"},
                {"yandex", "sauces", "Соусы"},
                {"yandex", "fillings", "Начинки"}
        });
    }

    @Before
    @DisplayName("Настройка тестового окружения")
    @Description("Инициализация драйвера, открытие главной страницы, принятие куки")
    public void setUp() {
        driver = DriverFactory.createDriver(browser);
        mainPage = new MainPage(driver);
        constructorPage = new ConstructorPage(driver);

        mainPage.open();
        mainPage.waitForLoad();
        mainPage.acceptCookies();
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
        String activeTab = constructorPage.getActiveTabText();
        assertEquals("Активная вкладка должна быть: " + expectedTab,
                expectedTab, activeTab);
    }

    @Step("Проверка видимости секции: {section}")
    private void verifySectionIsVisible(String section) {
        switch (section) {
            case "buns":
                assertTrue("Должна отображаться секция булок",
                        constructorPage.isBunsSectionVisible());
                break;
            case "sauces":
                assertTrue("Должна отображаться секция соусов",
                        constructorPage.isSaucesSectionVisible());
                break;
            case "fillings":
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