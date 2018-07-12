package pl.stochmialek.seleniumtests.bing;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.stream.Collectors;

class SearchAllPage extends SearchPage {

    static final String REGION_PL = "Tylko z regionu Polska";
    static final String REGION_ALL = "Wszystkie regiony";


    SearchAllPage(ChromeDriver driver) {
        super(driver);
    }

    void setRegionTo(String option) {
        driver.findElementByXPath("//*[@class='ftrB' and .//*[@id='ftrD_Region']]").click();
        driver.findElementByXPath("//*[@class='ftrD']//*[text()='" + option + "']").click();
    }

    List<String> getUrlResults() {
        List<WebElement> elements = driver.findElementsByCssSelector(".b_algo h2 a");
        return elements.stream()
                .map(element -> element.getAttribute("href"))
                .collect(Collectors.toList());
    }

    String getTitleByAHref(String a_href) {
        return driver.findElementByXPath("//*[@class='b_algo' and .//a[@href='" + a_href + "']]//h2").getText();
    }

    String getDescriptionByAHref(String a_href) {
        return driver.findElementByXPath("//*[@class='b_algo' and .//a[@href='" + a_href + "']]//p").getText();
    }

    Integer getPageNumber() {
        return Integer.parseInt(driver.findElementByCssSelector(".sb_pagS").getText());
    }

    String getPageNumberDescription() {
        return driver.findElementByCssSelector(".sb_count").getText();
    }

    void goToNextPage() {
        driver.findElementByCssSelector(".sb_pagN").click();
    }

    void goToPreviousPage() {
        driver.findElementByCssSelector(".sb_pagP").click();
    }

    void goToNPage(Integer n) {
        driver.findElementByXPath("//*[@class='b_pag']//*[contains(text(),'" + n + "')]").click();
    }

    String getIncorrectSearchWordText() {
        WebElement incorrectSearchWordText = driver.findElementByCssSelector("#sp_requery");
        return incorrectSearchWordText.getText();
    }

    SearchImagePage goToImagePageResults() {
        driver.findElementByXPath("//*[@class='b_scopebar']//*[text()='Obrazy']").click();
        return new SearchImagePage(driver);
    }

    SearchFirstPage goToFirstPage() {
        WebElement logo = driver.findElementByCssSelector(".b_logo");
        logo.click();
        return new SearchFirstPage(driver);
    }
}
