package util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver() {
        return createDriver("chrome");
    }

    public static WebDriver createDriver(String browser) {
        WebDriver driver;
        String browserLower = browser.toLowerCase();

        switch (browserLower) {
            case "chrome":
                driver = createChromeDriver();
                break;
            case "yandex":
                driver = createYandexDriver();
                break;
            default:
                System.out.println("Браузер '" + browser + "' не поддерживается. Используется Chrome.");
                driver = createChromeDriver();
        }

        // Общие настройки
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        return driver;
    }

    private static WebDriver createChromeDriver() {
        // Для Chrome 144 - используем Selenium Manager (он автоматически найдет драйвер)
        System.out.println("Создание драйвера для Chrome...");

        ChromeOptions options = new ChromeOptions();

        // Базовые настройки для Chrome
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    private static WebDriver createYandexDriver() {
        System.out.println("Создание драйвера для Яндекс.Браузера...");

        // ВАЖНО: УКАЖИТЕ ПРАВИЛЬНЫЙ ПУТЬ К chromedriver.exe для версии 142
        String chromeDriverPath = "C:\\Users\\admin\\Downloads\\chromedriver-win64\\chromedriver.exe"; // ИЗМЕНИТЕ НА СВОЙ ПУТЬ

        // Проверяем, существует ли файл
        java.io.File driverFile = new java.io.File(chromeDriverPath);
        if (!driverFile.exists()) {
            throw new RuntimeException("ChromeDriver для Яндекс.Браузера не найден по пути: " + chromeDriverPath +
                    "\nСкачайте ChromeDriver версии 142 с: https://chromedriver.chromium.org/downloads");
        }

        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.out.println("Используется ChromeDriver: " + chromeDriverPath);

        ChromeOptions options = new ChromeOptions();

        // Путь к Яндекс.Браузеру
        String yandexPath = "C:\\Program Files\\Yandex\\YandexBrowser\\Application\\browser.exe";
        options.setBinary(yandexPath);
        System.out.println("Используется Яндекс.Браузер: " + yandexPath);

        // Базовые настройки для Яндекс.Браузера
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }
}