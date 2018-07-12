package pl.stochmialek.seleniumtests.bing;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.stochmialek.seleniumtests.AbstractPage;

import java.util.List;
import java.util.stream.Collectors;

abstract class SearchPage extends AbstractPage {

    SearchPage(ChromeDriver driver) {
        super(driver);
    }

    String getDelHistoryConfirmationText() {
        WebElement delHistSuggestionText = driver.findElementByCssSelector(".sa_rmvd:first-child");
        return delHistSuggestionText.getText();
    }

    void delHistorySuggestions() {
        WebElement delHistorySuggestion = driver.findElementByCssSelector(".sa_tm.sa_tmHS .sa_rm");
        delHistorySuggestion.click();
    }

    String getHistorySuggestion() {
        return driver.findElementByCssSelector(".sa_tm.sa_tmHS").getText();
    }

    List<String> getSuggestions() {
        List<WebElement> elements = driver.findElementsByCssSelector(".sa_tm");
        return elements.stream()
                .map(element -> element.getText())
                .collect(Collectors.toList());
    }

    void assertThatHistorySuggestionsNotShown() {
        assertThatElementNotExist(".sa_tm.sa_tmHS");
    }

    private void assertThatElementNotExist(String locator) {
        try {
            driver.findElementByCssSelector(locator);
            Assertions.failBecauseExceptionWasNotThrown(NoSuchElementException.class);
        } catch (NoSuchElementException e) { /* ignore */ }
    }

    void fillSearchField(String word) {
        WebElement searchField = driver.findElement(By.id("sb_form_q"));
        searchField.sendKeys(word);
    }

    SearchAllPage submitSearchUsingEnter() {
        WebElement searchField = driver.findElementById("sb_form_q");
        searchField.sendKeys(Keys.ENTER);
        return new SearchAllPage(driver);
    }

    HistoryPage goToSearchHistory() {
        WebElement menuButton = driver.findElementById("id_sc");
        wait.until(ExpectedConditions.visibilityOf(menuButton));
        menuButton.click();
        driver.findElementByXPath("//*[@class='hb_titlerow']//*[text()='Historia wyszukiwania']").click();
        return new HistoryPage(driver);
    }

}
