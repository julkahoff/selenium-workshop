package pl.stochmialek.seleniumtests.bing;

import org.openqa.selenium.chrome.ChromeDriver;

class SearchFirstPage extends SearchPage {

    SearchFirstPage(ChromeDriver driver) {
        super(driver);
        super.driver.get("https://www.bing.com/?setmkt=pl-pl&setlang=pl-pl");
    }
}
