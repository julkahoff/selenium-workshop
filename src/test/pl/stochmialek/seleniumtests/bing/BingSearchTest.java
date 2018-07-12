package pl.stochmialek.seleniumtests.bing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BingSearchTest {

    private ChromeDriver driver;

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @After
    public void cleanup() {
        driver.close();
    }

    @Test
    public void suggestions() {
        SearchFirstPage searchFirstPage = new SearchFirstPage(driver);
        String searchWord = "pingwin";
        searchFirstPage.fillSearchField(searchWord);
        searchFirstPage.assertThatHistorySuggestionsNotShown();
        List<String> suggestions = searchFirstPage.getSuggestions();

        assertThat(suggestions).allMatch(element -> element.contains(searchWord));
        assertThat(suggestions).hasSize(8);

        SearchAllPage searchAllPage = searchFirstPage.submitSearchUsingEnter();
        searchAllPage.goToFirstPage();
        searchFirstPage.fillSearchField(searchWord);

        assertThat(searchFirstPage.getHistorySuggestion()).isNotEmpty();
        assertThat(searchFirstPage.getHistorySuggestion()).startsWith(searchWord);

        searchFirstPage.delHistorySuggestions();
        searchFirstPage.getDelHistoryConfirmationText();

        assertThat(searchFirstPage.getDelHistoryConfirmationText())
                .isEqualTo("To wyszukiwanie zostało usunięte z Twojej historii wyszukiwania");

        searchFirstPage.refresh();

        searchFirstPage.fillSearchField(searchWord);
        searchFirstPage.assertThatHistorySuggestionsNotShown();
    }

    @Test
    public void regionFilter() {
        SearchFirstPage searchPage =new SearchFirstPage(driver);
        String searchWord = "leanforge";
        String expectedUrl = "http://leanforge.pl/";
        searchPage.fillSearchField(searchWord);
        SearchAllPage searchAllPage= searchPage.submitSearchUsingEnter();

        assertThat(searchAllPage.getUrlResults()).contains(expectedUrl);
        assertThat(searchAllPage.getTitleByAHref(expectedUrl)).isEqualTo("LeanForge");
        assertThat(searchAllPage.getDescriptionByAHref(expectedUrl)).isEqualTo("LeanForge is a company focused on development of bespoke innovative software and sourcing of Java consultants. We are a team of programmers who share the same ...");

        searchAllPage.setRegionTo(SearchAllPage.REGION_PL);
        assertThat(searchAllPage.getUrlResults()).doesNotContain(expectedUrl);

        searchAllPage.setRegionTo(SearchAllPage.REGION_ALL);
        assertThat(searchAllPage.getUrlResults()).contains(expectedUrl);
    }

    @Test
    public void pagination() {
        SearchFirstPage searchPage =new SearchFirstPage(driver);
        String searchWord = "czy jest piatek";
        searchPage.fillSearchField(searchWord);
        SearchAllPage searchAllPage= searchPage.submitSearchUsingEnter();

        assertThat(searchAllPage.getPageNumber()).isEqualTo(1);
        assertThat(searchAllPage.getPageNumberDescription()).contains("Wyniki:");

        List<String> urlListFirstPage = searchAllPage.getUrlResults();

        searchAllPage.goToPreviousPage();
        assertThat(searchAllPage.getPageNumber()).isEqualTo(1);

        searchAllPage.goToNextPage();
        assertThat(searchAllPage.getPageNumber()).isEqualTo(2);
        assertThat(searchAllPage.getPageNumberDescription()).contains("11–20");
        assertThat(urlListFirstPage).isNotEqualTo(searchAllPage.getUrlResults());

        searchAllPage.goToPreviousPage();
        assertThat(searchAllPage.getPageNumber()).isEqualTo(1);

        searchAllPage.goToNPage(4);
        assertThat(searchAllPage.getPageNumber()).isEqualTo(4);
        assertThat(searchAllPage.getPageNumberDescription()).contains("31–40");
    }

    @Test
    public void incorrectSearchWord() {
        SearchFirstPage searchPage =new SearchFirstPage(driver);
        String searchWord = "nuwa zelandia";
        searchPage.fillSearchField(searchWord);
        SearchAllPage searchAllPage= searchPage.submitSearchUsingEnter();
        assertThat(searchAllPage.getIncorrectSearchWordText()).isEqualTo("W tym wyniki dla zapytania nowa zelandia.");

        SearchImagePage searchImagePage = searchAllPage.goToImagePageResults();
        assertThat(searchImagePage.getIncorrectSearchWordTextFromImageResultsPage()).isEqualTo("Uwzględniono wyniki dotyczące zapytania nowa zelandia.\n" +
                      "Pokaż tylko wyniki dotyczące zapytania nuwa zelandia.");
    }

    @Test
    public void imageResultsScrolling() {
        SearchFirstPage searchPage =new SearchFirstPage(driver);
        String searchWord = "bieszczady";
        searchPage.fillSearchField(searchWord);
        SearchAllPage searchAllPage= searchPage.submitSearchUsingEnter();
        SearchImagePage searchImagePage = searchAllPage.goToImagePageResults();
        Integer numberOfImages = searchImagePage.getImageResults().size();

        assertThat(numberOfImages).isPositive();

        searchPage.pageDown(2);
        searchImagePage.waitUntilPicturesAreLoadedMoreThen(numberOfImages);

        assertThat(searchImagePage.getImageResults().size())
                .describedAs("Extra images should be loaded when page is scrolled down.")
                .isGreaterThan(numberOfImages);
    }

    @Test
    public void searchHistory () {
        SearchFirstPage searchPage = new SearchFirstPage(driver);
        String searchWord = "costam";
        searchPage.fillSearchField(searchWord);
        searchPage.submitSearchUsingEnter();
        HistoryPage historyPage = searchPage.goToSearchHistory();

        assertThat(historyPage.getAllSearchHistoryElements()).contains(searchWord);

        historyPage.setHistoryTypeFilter(HistoryPage.TYPE_WEB);
        assertThat(historyPage.getAllSearchHistoryElements()).contains(searchWord);

        historyPage.setHistoryTypeFilter(HistoryPage.TYPE_IMAGE);
        assertThat(historyPage.getAllSearchHistoryElements()).doesNotContain(searchWord);

        historyPage.setHistoryTypeFilter(HistoryPage.TYPE_WEB);
        historyPage.deleteSearchHistory();
        assertThat(historyPage.getAllSearchHistoryElements()).doesNotContain(searchWord);
    }

}

