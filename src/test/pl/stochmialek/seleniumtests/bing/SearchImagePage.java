package pl.stochmialek.seleniumtests.bing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

class SearchImagePage extends SearchPage {

    SearchImagePage(ChromeDriver driver) {
        super(driver);
    }

    String getIncorrectSearchWordTextFromImageResultsPage() {
        WebElement incorrectSearchWordText = driver.findElementByCssSelector(".mmsp");
        return incorrectSearchWordText.getText();
    }

    List<String> getImageResults() {
        List<WebElement> elements = driver.findElementsByCssSelector("img.mimg");
        return elements.stream()
                .map(element -> element.getAttribute("src"))
                .collect(Collectors.toList());
    }

    void waitUntilPicturesAreLoadedMoreThen(Integer numberOfImages) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img.mimg"), numberOfImages));
    }
}
