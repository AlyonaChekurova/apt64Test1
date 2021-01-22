package pages;

import helpers.Waiters;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Start page object.
 */
public class StartPage {
    /**
     * Browser driver.
     */
    private WebDriver driver;

    /**
     * Maximum time to wait per seconds.
     */
    private final static int MAX_WAIT = 10;

    /**
     * Search input field.
     */
    private final static String SEARCH_INPUT_LOCATOR = ".input-search";

    /**
     * Authorization form window.
     */
    private final static String LOGIN_BOX_LOCATOR = ".login-box.logout-box";

    /**
     * Login field.
     */
    private final static String LOGIN_FIELD_LOCATOR = "input[name = 'login']";
    /**
     * Password field.
     */
    private final static String PASSWORD_FIELD_LOCATOR = "input[name = 'password']";

    /**
     * Preloader locator to check its invisibility.
     */
    private final static String PRELOADER_LOCATOR = ".preloader";

    /**
     * Login error message text locator.
     */
    private final static String LOGIN_ERROR_MESSAGE_LOCATOR = ".login-error";

    /**
     * Cart counter locator.
     */
    private final static String CART_COUNTER_LOCATOR = "#itogCount";

    /**
     * Button shows authorization form.
     */
    @FindBy(css = ".authorization.login-button")
    private WebElement authButton;

    /**
     * Login button.
     */
    @FindBy(css = "#signIn2")
    private WebElement loginButton;

    /**
     * Search results text.
     */
    @FindBy(css = ".search-word")
    private WebElement searchResult;

    /**
     * Add to cart button in the first search result.
     */
    @FindBy(css =
            ".search-block-name>.search-block>ul>li:nth-child(1)>div>.add-to-cart.buy")
    private WebElement addToCartButton;

    /**
     * Class constructor.
     * Initializes WebDriver instance.
     *
     * @param webDriver - WebDriver instance
     */
    public StartPage(WebDriver webDriver) {
        this.driver = webDriver;

        PageFactory.initElements(webDriver, this);
    }

    /**
     * Click button to show authorization form method.
     *
     * @return StartPage object
     */
    @Step("Нажать кнопку показа формы авторизации.")
    public final StartPage clickAuthButton() {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);

        Waiters.waitClickability(driver, MAX_WAIT, authButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(PRELOADER_LOCATOR)));
        authButton.click();
        ((JavascriptExecutor) driver).executeScript("window.stop();");
        return this;
    }

    /**
     * Try to authorize method.
     *
     * @param login    - login for authorization
     * @param password - password for authorization
     * @return error message text
     */
    @Step("Авторизоваться с логином '{login}' и паролем '{password}'")
    public final String checkAuth(final String login,
                                  final String password) {
        clickAuthButton();
        ((JavascriptExecutor) driver).executeScript("window.stop();");
        Waiters.waitVisibility(driver, MAX_WAIT,
                driver.findElement(By.cssSelector(LOGIN_BOX_LOCATOR)));

        driver.findElement(By.cssSelector(LOGIN_FIELD_LOCATOR)).sendKeys(login);
        ((JavascriptExecutor) driver).executeScript("window.stop();");
        driver.findElement(By.cssSelector(PASSWORD_FIELD_LOCATOR)).sendKeys(password);
        ((JavascriptExecutor) driver).executeScript("window.stop();");

        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        WebElement preloader = driver.findElement(By.cssSelector(PRELOADER_LOCATOR));
        if (preloader.isDisplayed())
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(PRELOADER_LOCATOR)));
        loginButton.click();

        WebElement errorMessage = driver.findElement(By.cssSelector
                (LOGIN_ERROR_MESSAGE_LOCATOR));
        Waiters.waitVisibility(driver, MAX_WAIT, driver
                .findElement(By.cssSelector(LOGIN_ERROR_MESSAGE_LOCATOR)));

        return errorMessage.getText();
    }

    /**
     * Get search results method.
     *
     * @param searchQuery - query to search for
     * @return StartPage object
     */
    @Step("Выполнить поиск по запросу {searchQuery}")
    public final StartPage getSearchResults(final String searchQuery) {
        driver.findElement(By.cssSelector(SEARCH_INPUT_LOCATOR)).clear();
        driver.findElement(By.cssSelector(SEARCH_INPUT_LOCATOR))
                .sendKeys(searchQuery + Keys.ENTER);
        Waiters.waitVisibility(driver, MAX_WAIT, searchResult);

        return this;
    }

    /**
     * Get search result text method.
     *
     * @param searchQuery - query to search for
     * @return search result text
     */
    @Step("Получить текст о результатах поиска по запросу {searchQuery}")
    public final String getSearchResultText(final String searchQuery) {
        getSearchResults(searchQuery);

        return searchResult.getText();
    }

    /**
     * Click add to cart button method.
     *
     * @param searchQuery - query to search for
     * @return StartPage object when count is changed
     */
    @Step("Выполнить поиск по запросу {searchQuery} и добавить в корзину товар, "
            + "первый по результатам поиска")
    public final StartPage clickAddToCartButton(final String searchQuery) {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        getSearchResults(searchQuery);
        ((JavascriptExecutor) driver).executeScript("window.stop();");
        final String currentText = driver.findElement(
                By.cssSelector(CART_COUNTER_LOCATOR)).getText();
        Waiters.waitClickability(driver, MAX_WAIT, addToCartButton);
        addToCartButton.click();

        wait.until((WebDriver driver) -> !driver.findElement(
                By.cssSelector(CART_COUNTER_LOCATOR))
                .getText().equals(currentText));

        return this;
    }

    /**
     * Get cart count value method.
     *
     * @return cart count value
     */
    @Step("Получить значение счетчика товаров в корзине")
    public final int getCartCount() {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        wait.withTimeout(Duration.ofSeconds(MAX_WAIT));
        WebElement cartCount = driver.findElement(
                By.cssSelector(CART_COUNTER_LOCATOR));

        return Integer.parseInt(cartCount.getText());
    }
}