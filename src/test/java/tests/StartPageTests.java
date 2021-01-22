package tests;

import helpers.DriverFactory;
import helpers.ParametersProvider;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.StartPage;

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
    private String browserName = System.getProperty("browserName");

    /**
     * Login to authorization test.
     */
    private String login = System.getProperty("login");

    /**
     * Password to authorization test.
     */
    private String password = System.getProperty("password");

    /**
     * Query text to search test.
     */
    private String query = System.getProperty("searchQuery");

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
     * Wrong data authorization test.
     *
     * @throws IOException when config file is unavailable
     */
    @Test(description = "Тест авторизации с некорректными логином и паролем")
    public final void wrongLoginPasswordTest() throws IOException {
        StartPage startPage = new StartPage(driver);

        String message = startPage.checkAuth(login, password);

        Assert.assertEquals(message,
                ParametersProvider.getProperty("loginErrorMessage"));
    }

    /**
     * Search field working test: successful search.
     */
    @Test(description = "Тест работы строки поиска: успешный поиск")
    public final void successfulSearchTest() {
        StartPage startPage = new StartPage(driver);

        String res = startPage.getSearchResultText(query);

        Assert.assertTrue(res.endsWith("НАЙДЕНО:"));
    }

    /**
     * Add to cart displaying test.
     */
    @Test(description = "Тест отображения добавления в корзину")
    public final void addToCartTest() {
        StartPage startPage = new StartPage(driver);

        int initCount = startPage.getCartCount();


        int finalCount = startPage.clickAddToCartButton(query)
                .getCartCount();

        Assert.assertEquals(finalCount, initCount + 1);

    }

    /**
     * Get page title test.
     */
    @Test(description = "Тест заголовка страницы")
    public final void checkPageTitle() {
        String pageTitle = driver.getTitle();

        Assert.assertNotEquals(driver.getTitle(), pageTitle);
    }

    /**
     * Suite teardown.
     */
    @AfterClass
    public final void tearDown() {
        driver.quit();
    }

}