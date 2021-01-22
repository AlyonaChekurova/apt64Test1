package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Browser driver factory.
 */
public final class DriverFactory {
    /**
     * Not called.
     */
    private DriverFactory() {
    }

    /**
     * Sets driver path if it possible.
     *
     * @param browserName browser name
     * @throws IOException when config file is not available
     */
    private static void trySetDriverPath(final String browserName)
            throws IOException {
        String driverPath = ParametersProvider.getProperty("driverPath");
        if (browserName.equals("chrome"))
            driverPath += "chromedriver.exe";

        if (browserName.equals("firefox"))
            driverPath += "geckodriver.exe";

        if (!driverPath.isEmpty()) {
            System.setProperty("webdriver." + browserName + ".driver", driverPath);
        }
    }

    /**
     * Get driver
     *
     * @param browserName browser name
     */
    private static WebDriver getDriver(String browserName) {
        switch (browserName) {
            case "chrome":
            case "opera":
                return new ChromeDriver();
            case "firefox":
                return new FirefoxDriver();
            case "edge":
                return new EdgeDriver();
            default:
                throw new IllegalStateException("Chosen browser not supported");
        }
    }

    /**
     * Creates a browser driver using configuration.
     *
     * @return browser driver
     * @throws IOException           when config file is not available
     * @throws IllegalStateException when unsupported browser chosen
     */
    public static WebDriver createDriver(String browserName) throws IOException,
            IllegalStateException {
        WebDriver driver;

        int pageLoadTimeout = Integer.parseInt(ParametersProvider
                .getProperty("pageLoadTimeout"));

        trySetDriverPath(browserName);
        driver = getDriver(browserName);

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout,
                TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(pageLoadTimeout,
                TimeUnit.SECONDS);

        return driver;
    }
}