package tests.ui;

import factory.DataFactory;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WorkspaceTest extends BaseTest {

    DataFactory data = new DataFactory();
    File pic = new File("src/test/resources/images/6.jpg");
    private final String
            filterName = data.generateTag(),
            secondFilterName = data.generateTag(),
            description = data.generateDescription();

    @Test(
            testName = "Check filtering by tag",
            groups = {"Regression"},
            priority = 1
    )
    public void checkFilteringByTag() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId);
        soft.assertThat(workspacePage.getCountCardByTag(cardTag)).isEqualTo(2);
        workspacePage.filteringByTag(cardTag);
        soft.assertThat(workspacePage.getCountCardByTag(cardTag)).isEqualTo(2);
        soft.assertAll();
    }

    @Test(
            testName = "Check filtering by card name",
            groups = {"Regression"},
            priority = 2
    )
    public void checkFilteringByCardName() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId);
        soft.assertThat(workspacePage.getCountCardByTitle(firstCardName)).isEqualTo(2);
        workspacePage.filteringByTitle(firstCardName);
        soft.assertThat(workspacePage.getCountCardByTitle(firstCardName)).isEqualTo(2);
        soft.assertAll();
    }

    @Test(
            testName = "Check search by card name",
            groups = {"Regression"},
            priority = 3
    )
    public void checkSearchByCardName() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId);
        soft.assertThat(workspacePage.getCountCardByTitle(firstCardName)).isEqualTo(2);
        workspacePage.searchByText(firstCardName, workspaceName);
        soft.assertThat(workspacePage.getNumberOfFoundCards()).isEqualTo(2);
        soft.assertAll();
    }

    @Test(
            testName = "Check creating custom filter",
            groups = {"Regression"},
            priority = 4
    )
    public void checkCreatingCustomFilter() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId)
                .filteringByTag(cardTag)
                .filteringByTitle(firstCardName)
                .saveCustomFilter(filterName);
        assertThat(workspacePage.getCustomFilters().contains(filterName)).isTrue();
    }

    @Test(
            testName = "Check filtering by custom filter",
            groups = {"Regression"},
            priority = 5
    )
    public void checkSearchByCustomFilter() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId);
        soft.assertThat(workspacePage.getCountCardByTitleAndTag(firstCardName, cardTag)).isEqualTo(2);
        workspacePage
                .filteringByTag(cardTag)
                .filteringByTitle(firstCardName)
                .saveCustomFilter(secondFilterName)
                .clearFilters()
                .chooseCustomFilter(secondFilterName);
        soft.assertThat(workspacePage.getCountCards()).isEqualTo(2);
        soft.assertAll();
    }

    @Test(
            testName = "Check opening card",
            groups = {"Regression"},
            priority = 6
    )
    public void checkOpenCard() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId)
                .openCardByIndex(5);
        assertThat(workspacePage.isCardModalOpened()).isTrue();
    }

    @Test(
            testName = "Check closing card",
            groups = {"Regression"},
            priority = 7
    )
    public void checkCloseCard() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace(workspace, workspaceId)
                .openCardByIndex(5)
                .cardIsOpened()
                .closeCardModal()
                .cardModalIsClosed();
        assertThat(workspacePage.isCardModalOpened()).isFalse();
    }

    @Test(
            testName = "Check drag and drop card",
            groups = {"Regression"},
            priority = 8
    )
    public void checkDragAndDropCard() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .openWorkspace(workspace, workspaceId);
        soft.assertThat(workspacePage.getCountCardsInColumn(1)).isEqualTo(2);
        workspacePage
                .dragAndDropCardToColumn(0, 3);
        soft.assertThat(workspacePage.getCountCardsInColumn(3)).isEqualTo(3);
        soft.assertAll();
    }

    @Test(
            testName = "Check adding comment to card",
            groups = {"Regression"},
            priority = 9
    )
    public void checkAddComment() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .openWorkspace(workspace, workspaceId)
                .openCardByIndex(0)
                .cardIsOpened()
                .addComment(description);
        assertThat(workspacePage.isCommentAdded(description)).isTrue();
    }

    @Test(
            testName = "Check adding comment with file to card",
            groups = {"Regression"},
            priority = 10
    )
    public void checkAddCommentWithFile() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .openWorkspace(workspace, workspaceId)
                .openCardByIndex(0)
                .cardIsOpened()
                .addComment(description, pic);
        assertThat(workspacePage.isCommentAdded(description, pic.getName())).isTrue();
    }
}
