package tests;

import helpers.DriverFactory;
import helpers.ParametersProvider;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

/**
 * Test suite for start page.
 */
public class StartPageTests {

    /**
     * Browser driver.
     */
    private WebDriver driver;

    /**
     * Name of browser where tests should run.
     */
    private String browserName = "chrome";

    /**
     * Suite setup.
     *
     * @throws IOException when config file is unavailable
     */
    @BeforeTest(description = "Настройка окружения: создание драйвера браузера")
    public final void setEnvironment() throws IOException {
        this.driver = DriverFactory.createDriver(browserName);
        String webUrl = ParametersProvider.getProperty("webUrl");
        driver.get(webUrl);
    }


    /**
     * Suite teardown.
     */
    @AfterClass
    public final void tearDown() {
        driver.quit();
    }

}
