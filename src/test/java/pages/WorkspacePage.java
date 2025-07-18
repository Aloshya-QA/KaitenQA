package pages;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Log4j2
public class WorkspacePage {

    public WorkspacePage openWorkspace(String workspaceId) {
        open(format("https://lynchhuel4819.kaiten.ru/space/%s/boards", workspaceId));
        return this;
    }

    public WorkspacePage isOpened() {
        try {
            $("[data-testid='app-bar-profile-avatar']").shouldBe(visible);
            log.info("WorkspacePage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("WorkspacePage isn't opened");
        }

        return this;
    }

    public boolean isWorkspaceOpened() {
        log.warn("Checking the status of opening WorkspacePage...");
        try {
            $("[data-testid='app-bar-profile-avatar']").shouldBe(visible);
            log.warn("Is WorkspacePage: True");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Is WorkspacePage: False");
        }

        return true;
    }

    public WorkspacePage filteringByTag(String tag) {
        $("[data-testid='open-filters-button']").click();
        $("[data-testid='predicate-builder']").shouldBe(visible);
        $("[data-testid='filters-predication-tag']").click();
        $("[placeholder='Выберите метку']").shouldBe(visible).click();
        $("[data-testid='card-tag-title']").shouldBe(visible);
        $$("div[data-testid='card-tag-title']")
                .findBy(text(tag))
                .shouldBe(visible)
                .click();
        $$("div[data-testid='filters-dialog'] button")
                .findBy(text("Добавить"))
                .shouldBe(clickable)
                .click();
        $("[aria-label='Карточек отфильтровано / всего']").shouldBe(visible);
        return this;
    }

    public WorkspacePage filteringByTitle(String title) {
        $("[data-testid='open-filters-button']").click();
        $("[data-testid='predicate-builder']").shouldBe(visible);
        $("[data-testid='filters-predication-title']").click();
        $x("//div[@data-testid='filters-dialog']//input[@type='text']").shouldBe(visible).setValue(title);
        $$("div[data-testid='filters-dialog'] button")
                .findBy(text("Добавить"))
                .shouldBe(clickable)
                .click();
        $("[aria-label='Карточек отфильтровано / всего']").shouldBe(visible);
        return this;
    }

    public WorkspacePage searchByText(String title, String workspaceName) {
        $("[data-testid='SearchIcon']").shouldBe(visible).click();
        $x("//input[@placeholder='Во всех']").shouldBe(visible).setValue(workspaceName);
        $x(format("//li[text()='%s']", workspaceName)).shouldBe(visible).click();
        $x("//input[@placeholder='Текст для поиска']").setValue(title);
        return this;
    }

    public int getNumberOfFoundCards() {
        $(byTagName("circle")).shouldBe(visible).shouldNotBe(visible);
        return $$x("//div[@data-testid='card-with-fixed-height']").size();
    }

    public int getCountCardByTag(String tag) {
        $("[data-test='lane']").shouldBe(visible);
        return $$x(format("//div[@data-testid='card-facade-tags']//span[text()='%s']", tag)).size();
    }

    public int getCountCardByTitle(String title) {
        $("[data-test='lane']").shouldBe(visible);
        return $$x(format("//span[@data-testid='card-title-text' and contains(text(), '%s')]", title)).size();
    }

    public int getCountCardByTitleAndTag(String title, String tag) {
        int counter = 0;
        $("[data-card-id]").shouldBe(visible);
        ElementsCollection cards = $$("[data-card-id]");
        System.out.println("Cards size: " + cards.size());
        for (SelenideElement card : cards) {
            String cardTitle = card.$("[data-testid='card-title-text']").shouldBe(visible).getText();
            String cardTag = card.$("[data-testid='card-facade-tags'] span").shouldBe(visible).getText();
            System.out.println("Card title: " + cardTitle);
            System.out.println("Card tag: " + cardTag);
            if (cardTitle.equals(title) && cardTag.equals(tag)) {
                counter++;
            }
        }
        return counter;
    }

    public WorkspacePage clearFilters() {
        $x("//span[text()='Очистить']").shouldBe(visible).click();
        return this;
    }

    public WorkspacePage saveCustomFilter(String filterName) {
        $("[aria-label='Сохранить набор фильтров']").shouldBe(visible).click();
        $(byText("Сохранить набор фильтров")).shouldBe(visible);
        $("textarea").setValue(filterName);
        $x("//button[text()='Сохранить']").shouldBe(enabled).click();
        return this;
    }

    public int getCountCards() {
        $("[data-card-id]").shouldBe(visible);
        return $$("[data-card-id]").size();
    }

    public WorkspacePage openCardByIndex(int index) {
        $("[data-card-id]").shouldBe(visible);
        $$x("//div[@data-card-id]//a[@href and not(@data-testid='card-facade-external-link')]").get(index).click();
        return this;
    }

    public int getCountCardsInColumn(int indexColumn) {
        $("[data-card-id]").shouldBe(visible);
        int numberOfColumn = 0;
        SelenideElement column = $$("div[data-testid^='column']:not([data-testid*='title'])").get(indexColumn);
        int cardsInColumn = column.$$("[data-card-id]").size();
        numberOfColumn += cardsInColumn;
        return numberOfColumn;
    }

    public WorkspacePage addComment(String text) {
        $("[data-testid='new-line']").shouldBe(visible).setValue(text);
        $("[data-testid='save-comment-button']").shouldBe(clickable).click();
        return this;
    }

    public WorkspacePage addFileInComment(File file) {
        $("[data-testid='new-line']").shouldBe(visible).pressEnter();
        $("[data-testid='new-comment-editor-actions']").shouldBe(visible);
        $("[data-test='comment-file-input']").uploadFile(file);
        $("[data-testid='save-comment-button']").shouldBe(clickable).click();
        return this;
    }

    public WorkspacePage dragAndDropCardToColumn(int indexCard, int indexColumn) {
        $("[data-card-id]").shouldBe(visible);

        SelenideElement card = $$x("//div[@data-card-id]//a[@href and not(@data-testid='card-facade-external-link')]").get(indexCard);
        SelenideElement column = $$x("//div[starts-with(@data-testid, 'column') and not(contains(@data-testid, 'title'))]/div").get(indexColumn);
        card.dragAndDrop(DragAndDropOptions.to(column));
        return this;
    }

    public WorkspacePage cardIsOpened() {
        $("[data-testid='close-button']").shouldBe(visible);
        return this;
    }

    public WorkspacePage closeCardModal() {
        $("[data-testid='close-button']").click();
        return this;
    }

    public boolean isCardModalOpened() {
        return $x("//div[@data-testid='sentinelStart']/following-sibling::div[not(@data-testid='sentinelEnd')]").is(visible);
    }

    public WorkspacePage cardModalIsClosed() {
        $("[data-testid='close-button']").shouldNotBe(visible);
        return this;
    }

    public List<String> getCustomFilters() {
        $("[data-testid='open-filters-button']").click();
        $("[data-testid='predicate-builder']").shouldBe(visible);
        List<String> filtersName = new ArrayList<>();
        ElementsCollection customFilters = $$x("//li[text()='Мои наборы фильтров']/following-sibling::li");
        for (SelenideElement customFilter : customFilters) {
            filtersName.add(customFilter.getText());
            log.info(customFilter.getText());
        }
        return filtersName;
    }

    public WorkspacePage chooseCustomFilter(String filterName) {
        $("[data-testid='open-filters-button']").click();
        $("[data-testid='predicate-builder']").shouldBe(visible);
        ElementsCollection customFilters = $$x("//li[text()='Мои наборы фильтров']/following-sibling::li");
        for (SelenideElement customFilter : customFilters) {
            if (customFilter.getText().equals(filterName)) {
                customFilter.$("div").click();
            }
            break;
        }
        refresh();
        return this;
    }

    public LoginPage logout() {
        log.info("Logout...");
        $("[data-testid='app-bar-profile-avatar']").click();
        $("[data-testid='app-bar-profile-logout-button']").shouldBe(visible).click();
        return new LoginPage();
    }
}
