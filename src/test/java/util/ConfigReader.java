package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Файл конфигурации " + CONFIG_FILE + " не найден. Используются значения по умолчанию.");
                setDefaultProperties();
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке конфигурации: " + e.getMessage());
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("base.url", "https://stellarburgers.education-services.ru/");
        properties.setProperty("timeout.implicit", "10");
        properties.setProperty("timeout.page.load", "30");
        properties.setProperty("timeout.script", "30");
        properties.setProperty("chrome.options", "--no-sandbox,--disable-dev-shm-usage,--start-maximized,--remote-allow-origins=*");
        properties.setProperty("yandex.browser.path", "C:\\Program Files\\Yandex\\YandexBrowser\\Application\\browser.exe");
    }

    public static String getBrowser() {
        // Проверяем системную переменную (приоритет 1)
        String browserFromEnv = System.getenv("BROWSER");
        if (browserFromEnv != null && !browserFromEnv.trim().isEmpty()) {
            return browserFromEnv.trim().toLowerCase();
        }

        // Проверяем системное свойство (приоритет 2)
        String browserFromProperty = System.getProperty("browser");
        if (browserFromProperty != null && !browserFromProperty.trim().isEmpty()) {
            return browserFromProperty.trim().toLowerCase();
        }

        // Используем значение из файла конфигурации (приоритет 3)
        return properties.getProperty("browser", "chrome").toLowerCase();
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static int getImplicitTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.implicit"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.page.load"));
    }

    public static String[] getChromeOptions() {
        return properties.getProperty("chrome.options").split(",");
    }

    public static String getYandexBrowserPath() {
        return properties.getProperty("yandex.browser.path");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}