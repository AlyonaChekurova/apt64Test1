package helpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Helper class for custom explicit waits.
 */
public final class Waiters {
    /**
     * Not called.
     */
    private Waiters() {
    }

    /**
     * Wait for Angular pending requests to finish.
     *
     * @param webDriver browser driver
     * @param timeout   wait threshold
     */
    public static void waitUntilAngularReady(final WebDriver webDriver,
                                             final int timeout) {
        final String angularReady =
                "return angular.element(document).injector()"
                        + ".get('$http').pendingRequests.length === 0";
        ExpectedCondition<Boolean> angularLoad = driver ->
                Boolean.valueOf(((JavascriptExecutor) driver)
                        .executeScript(angularReady).toString());
        new WebDriverWait(webDriver, timeout).until(angularLoad);
    }

    /**
     * Wait for visibility of page element.
     *
     * @param driver  browser driver
     * @param time    waiting time per seconds
     * @param element page element
     */
    public static void waitVisibility(
            final WebDriver driver, final int time, final WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for clickability of page element specified by locator.
     *
     * @param driver  browser driver
     * @param time    waiting time per seconds
     * @param element page element
     */
    public static void waitClickability(
            final WebDriver driver, final int time, final WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}
