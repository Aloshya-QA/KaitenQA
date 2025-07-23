package pages;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Log4j2
public class WorkspacePage {

    private static final String
            PROFILE_AVATAR = "[data-testid='app-bar-profile-avatar']",
            FILTERS_CLEAR_BUTTON = "//span[text()='Очистить']",
            FILTERS_BUTTON = "[data-testid='open-filters-button']",
            FILTERS_MODAL = "[data-testid='predicate-builder']",
            FILTERS_TAG_BUTTON = "[data-testid='filters-predication-tag']",
            FILTERS_TAG_INPUT = "[placeholder='Выберите метку']",
            FILTERS_TAG_LIST = "[data-testid='card-tag-title']",
            FILTERS_ADD_BUTTON = "div[data-testid='filters-dialog'] button",
            FILTERS_LABEL = "[aria-label='Карточек отфильтровано / всего']",
            FILTERS_TITLE_BUTTON = "[data-testid='filters-predication-title']",
            FILTERS_TITLE_INPUT = "//div[@data-testid='filters-dialog']//input[@type='text']",
            SEARCH_BUTTON = "[data-testid='SearchIcon']",
            SEARCH_SPACE_INPUT = "//input[@placeholder='Во всех']",
            SEARCH_SPACE_LIST = "//li[text()='%s']",
            SEARCH_INPUT = "//input[@placeholder='Текст для поиска']",
            SEARCH_FOUND_LIST = "//div[@data-testid='card-with-fixed-height']",
            CARD_TAG_SEARCH = "//div[@data-testid='card-facade-tags']//span[text()='%s']",
            CARD_LANES = "[data-test='lane']",
            CARD_TITLE_LIST = "//span[@data-testid='card-title-text' and contains(text(), '%s')]",
            CARD_LIST = "[data-card-id]",
            CARD_TITLE = "[data-testid='card-title-text']",
            CARD_TAG_LIST = "[data-testid='card-facade-tags'] span",
            SAVE_CUSTOM_FILTER_BUTTON = "[aria-label='Сохранить набор фильтров']",
            CUSTOM_FILTER_NAME_INPUT = "textarea",
            CUSTOM_FILTER_SAVE_BUTTON = "//button[text()='Сохранить']",
            CARD_SELECT_LIST = "//div[@data-card-id]" +
                    "//a[@href and not(@data-testid='card-facade-external-link')]",
            COLUMN_LIST = "div[data-testid^='column']:not([data-testid*='title'])",
            COMMENT_INPUT = "[data-testid='new-line']",
            COMMENT_SAVE_BUTTON = "[data-testid='save-comment-button']",
            COMMENT_ACTIONS_BAR = "[data-testid='new-comment-editor-actions']",
            COMMENT_FILE_INPUT = "[data-test='comment-file-input']",
            COMMENT_SELECT_LIST = "//div[@data-testid='card-comment-text']//p[text()='%s']",
            COMMENT_FILE_LINK = "div[data-test^='file-'] > a",
            CARD_COMMENT_LIST = "[data-testid='card-comment']",
            COLUMN_SELECT_LIST = "//div[starts-with(@data-testid, 'column') " +
                    "and not(contains(@data-testid, 'title'))][%s]/div",
            CARD_TITLE_FROM_LIST = "parent::div//span[@data-testid='card-title-text']",
            CARD_CLOSE_BUTTON = "[data-testid='close-button']",
            CARD_MODAL = "//div[@data-testid='sentinelStart']" +
                    "/following-sibling::div[not(@data-testid='sentinelEnd')]",
            CUSTOM_FILTER_LIST = "//li[text()='Мои наборы фильтров']/following-sibling::li",
            PROFILE_LOGOUT_BUTTON = "[data-testid='app-bar-profile-logout-button']";

    @Step("Opening workspace...")
    public WorkspacePage openWorkspace(String workspace, String workspaceId) {
        log.info("Opening workspace...");
        open(format("https://%s.kaiten.ru/space/%s/boards", workspace, workspaceId));
        return this;
    }

    @Step("WorkspacePage is opened")
    public WorkspacePage isOpened() {
        try {
            $(PROFILE_AVATAR).shouldBe(visible);
            log.info("WorkspacePage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("WorkspacePage isn't opened");
        }

        return this;
    }

    @Step("Checking the status of opening WorkspacePage...")
    public boolean isWorkspaceOpened() {
        log.warn("Checking the status of opening WorkspacePage...");
        return $(PROFILE_AVATAR).is(visible);
    }

    @Step("Filtering cards by tag: '{tag}'...")
    public WorkspacePage filteringByTag(String tag) {
        log.info("Filtering cards by tag: '{}'...", tag);
        $(FILTERS_BUTTON).click();
        $(FILTERS_MODAL).shouldBe(visible);
        $(FILTERS_TAG_BUTTON).click();
        $(FILTERS_TAG_INPUT).shouldBe(visible).click();
        $(FILTERS_TAG_LIST).shouldBe(visible);
        $$(FILTERS_TAG_LIST)
                .findBy(text(tag))
                .shouldBe(visible)
                .click();
        $$(FILTERS_ADD_BUTTON)
                .findBy(text("Добавить"))
                .shouldBe(clickable)
                .click();
        $(FILTERS_LABEL).shouldBe(visible);
        return this;
    }

    @Step("Filtering cards by title: '{title}'...")
    public WorkspacePage filteringByTitle(String title) {
        log.info("Filtering cards by title: '{}'...", title);
        $(FILTERS_BUTTON).click();
        $(FILTERS_MODAL).shouldBe(visible);
        $(FILTERS_TITLE_BUTTON).click();
        $x(FILTERS_TITLE_INPUT).shouldBe(visible).setValue(title);
        $$(FILTERS_ADD_BUTTON)
                .findBy(text("Добавить"))
                .shouldBe(clickable)
                .click();
        $(FILTERS_LABEL).shouldBe(visible);
        return this;
    }

    @Step("Searching card by title: '{title}'...")
    public WorkspacePage searchByText(String title, String workspaceName) {
        log.info("Searching card by title: '{}'...", title);
        $(SEARCH_BUTTON).shouldBe(visible).click();
        $x(SEARCH_SPACE_INPUT).shouldBe(visible).setValue(workspaceName);
        $x(format(SEARCH_SPACE_LIST, workspaceName)).shouldBe(visible).click();
        $x(SEARCH_INPUT).setValue(title);
        return this;
    }

    @Step("Getting number of found cards...")
    public int getNumberOfFoundCards() throws InterruptedException {
        Thread.sleep(2000);
        log.info("Getting number of found cards...");
        $(byTagName("circle")).shouldBe(visible).shouldNotBe(visible);
        return $$x(SEARCH_FOUND_LIST).size();
    }

    @Step("Getting count cards by tag: '{tag}'...")
    public int getCountCardByTag(String tag) throws InterruptedException {
        Thread.sleep(2000);
        log.info("Getting count cards by tag: '{}'...", tag);
        $(CARD_LANES).shouldBe(visible);
        return $$x(format(CARD_TAG_SEARCH, tag)).size();
    }

    @Step("Getting count cards by title: '{title}'...")
    public int getCountCardByTitle(String title) throws InterruptedException {
        Thread.sleep(2000);
        log.info("Getting count cards by title: '{}'...", title);
        $(CARD_LANES).shouldBe(visible);
        return $$x(format(CARD_TITLE_LIST, title)).size();
    }

    @Step("Getting count cards by title and tag: '{title}', '{tag}'...")
    public int getCountCardByTitleAndTag(String title, String tag) {
        log.info("Getting count cards by title and tag: '{}', '{}'...", title, tag);
        int counter = 0;
        $(CARD_LIST).shouldBe(visible);
        ElementsCollection cards = $$(CARD_LIST);
        for (SelenideElement card : cards) {
            String cardTitle = card.$(CARD_TITLE).shouldBe(visible).getText();
            ElementsCollection allCardTags = card.$$(CARD_TAG_LIST);
            String cardTag;
            for (SelenideElement cardTags : allCardTags) {
                cardTag = cardTags.getText();
                if (cardTitle.equals(title) && cardTag.equals(tag)) {
                    counter++;
                }
            }
        }

        return counter;
    }

    @Step("Clear filters...")
    public WorkspacePage clearFilters() {
        log.info("Clear filters...");
        $x(FILTERS_CLEAR_BUTTON).shouldBe(visible).click();
        return this;
    }

    @Step("Saving custom filter: '{filterName}'...")
    public WorkspacePage saveCustomFilter(String filterName) {
        log.info("Saving custom filter: '{}'...", filterName);
        $(SAVE_CUSTOM_FILTER_BUTTON).shouldBe(visible).click();
        $(byText("Сохранить набор фильтров")).shouldBe(visible);
        $(CUSTOM_FILTER_NAME_INPUT).setValue(filterName);
        $x(CUSTOM_FILTER_SAVE_BUTTON).shouldBe(enabled).click();
        return this;
    }

    @Step("Getting count cards...")
    public int getCountCards() {
        log.info("Getting count cards...");
        $(CARD_LIST).shouldBe(visible);
        return $$(CARD_LIST).size();
    }

    @Step("Opening card by index #{index}...")
    public WorkspacePage openCardByIndex(int index) {
        log.info("Opening card by index #{}...", index);
        $(CARD_LIST).shouldBe(visible);
        $$x(CARD_SELECT_LIST).get(index).click();
        return this;
    }

    @Step("Getting count cards in #{column} column...")
    public int getCountCardsInColumn(int column) throws InterruptedException {
        log.info("Getting count cards in #{} column...", column);
        Thread.sleep(2000);
        $(CARD_LIST).shouldBe(visible);
        ElementsCollection columns = $$(COLUMN_LIST);
        return columns.get(column - 1).$$(CARD_LIST).size();
    }

    @Step("Adding comment to card...")
    public WorkspacePage addComment(String text) {
        log.info("Adding comment to card...");
        $(COMMENT_INPUT).shouldBe(visible).setValue(text);
        $(COMMENT_SAVE_BUTTON).shouldBe(clickable).click();
        return this;
    }

    @Step("Adding comment with file to card...")
    public WorkspacePage addComment(String text, File file) {
        log.info("Adding comment with file to card...");
        $(COMMENT_INPUT).shouldBe(visible).setValue(text);
        $(COMMENT_ACTIONS_BAR).shouldBe(visible);
        $(COMMENT_FILE_INPUT).uploadFile(file);
        $(COMMENT_SAVE_BUTTON).shouldBe(clickable).click();
        $(byText("несколько секунд назад")).shouldBe(visible);
        return this;
    }

    @Step("Checking comment added status...")
    public boolean isCommentAdded(String text) {
        log.info("Checking comment added status...");
        return $x(format(COMMENT_SELECT_LIST, text)).shouldBe(visible).is(visible);
    }

    @Step("Checking comment added status...")
    public boolean isCommentAdded(String text, String fileName) {
        $(byText("Файл(ы) загружен.")).shouldBe(visible);
        $(byText("Файл(ы) загружен.")).shouldNotBe(visible);
        $x(format(COMMENT_SELECT_LIST, text)).shouldBe(visible);
        log.info("Checking comment added status...");
        ElementsCollection comments = $$(CARD_COMMENT_LIST);
        for (SelenideElement comment : comments) {
            boolean isTextExist = comment.$(byText(text)).is(exist);
            String href = comment.$(COMMENT_FILE_LINK).getAttribute("href");
            String fileTitle = href.substring(href.indexOf("name=") + 5);
            return isTextExist && fileTitle.equals(fileName);
        }

        return false;
    }

    @Step("Dragging card #{indexCard} to column #{targetColumn}...")
    public WorkspacePage dragAndDropCardToColumn(int indexCard, int targetColumn) throws InterruptedException {
        log.info("Dragging card #{} to column #{}...", indexCard, targetColumn);
        $(CARD_LIST).shouldBe(visible);
        ElementsCollection cards = $$x(CARD_SELECT_LIST);
        SelenideElement targetColumnElement = $x(format(COLUMN_SELECT_LIST, targetColumn));
        SelenideElement card = cards.get(indexCard);
        String targetCardName = card.find(byXpath(CARD_TITLE_FROM_LIST)).getText();
        cards.get(indexCard).dragAndDrop(DragAndDropOptions.to(targetColumnElement));
        targetColumnElement.find(byText(targetCardName)).shouldHave(visible);
        Thread.sleep(3000);
        return this;
    }

    @Step("Card is opened")
    public WorkspacePage cardIsOpened() {
        log.info("Checking opening card status...");
        try {
            $(CARD_CLOSE_BUTTON).shouldBe(visible);
            log.info("Card is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Card isn't opened");
        }

        return this;
    }

    @Step("Closing card...")
    public WorkspacePage closeCardModal() {
        log.info("Closing card...");
        $(CARD_CLOSE_BUTTON).click();
        return this;
    }

    @Step("Checking opening card status...")
    public boolean isCardModalOpened() {
        log.info("Checking opening card status...");
        return $x(CARD_MODAL).is(visible);
    }

    @Step("Card is closed")
    public WorkspacePage cardModalIsClosed() {
        try {
            $(CARD_CLOSE_BUTTON).shouldNotBe(visible);
            log.info("Card is closed");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Card isn't closed");
        }

        return this;
    }

    @Step("Getting custom filters name...")
    public List<String> getCustomFilters() {
        log.info("Getting custom filters name...");
        $(FILTERS_BUTTON).click();
        $(FILTERS_MODAL).shouldBe(visible);
        List<String> filtersName = new ArrayList<>();
        ElementsCollection customFilters = $$x(CUSTOM_FILTER_LIST);
        for (SelenideElement customFilter : customFilters) {
            filtersName.add(customFilter.getText());
        }

        return filtersName;
    }

    @Step("Choosing custom filter by name: '{filterName}'...")
    public WorkspacePage chooseCustomFilter(String filterName) {
        log.info("Choosing custom filter by name: '{}'...", filterName);
        $(FILTERS_BUTTON).click();
        $(FILTERS_MODAL).shouldBe(visible);
        ElementsCollection customFilters = $$x(CUSTOM_FILTER_LIST);
        for (SelenideElement customFilter : customFilters) {
            if (customFilter.getText().equals(filterName)) {
                customFilter.$("div").click();
            }
            break;
        }

        refresh();
        return this;
    }

    @Step("Logout...")
    public LoginPage logout() {
        log.info("Logout...");
        $(PROFILE_AVATAR).click();
        $(PROFILE_LOGOUT_BUTTON).shouldBe(visible).click();
        return new LoginPage();
    }
}
