package pl.stochmialek.seleniumtests;

import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPage {
    protected ChromeDriver driver;
    protected WebDriverWait wait;

    protected AbstractPage(ChromeDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(this.driver, 5);
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void pageDown(Integer nTimes) {
        for (int i = 0; i < nTimes; i++) {
            driver.getKeyboard().sendKeys(Keys.PAGE_DOWN);
        }
    }

}
