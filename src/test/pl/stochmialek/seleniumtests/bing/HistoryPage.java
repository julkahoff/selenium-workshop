package pl.stochmialek.seleniumtests.bing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.stochmialek.seleniumtests.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

class HistoryPage extends AbstractPage {
    static final String TYPE_WEB = "Sieć Web";
    static final String TYPE_IMAGE = "Obrazy";

    HistoryPage(ChromeDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("HistoryViewer_HistorySettings")));
    }

    List<String> getAllSearchHistoryElements() {
        List<WebElement> elements = driver.findElementsByXPath("//*[@class='hv-query']");
        return elements.stream()
                .map(element -> element.getText())
                .collect(Collectors.toList());
    }

    void deleteSearchHistory() {
        driver.findElementById("CLRH_toggleModal").click();
        driver.findElementById("CLRH_confirm").click();
    }

    void setHistoryTypeFilter(String type) {
        driver.findElementByCssSelector(".hv-toolbar-link[title='Filtruj historię wyszukiwania wg typu']").click();
        driver.findElementByXPath("//*[@class='dropdown-base']//*[text()='"+type+"']").click();
    }
}
