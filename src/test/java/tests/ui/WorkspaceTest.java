package tests.ui;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WorkspaceTest extends BaseTest {

    File pic = new File("src/test/resources/images/photo.jpg");

    @Test
    public void checkFilteringByTag() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992");
        soft.assertThat(workspacePage.getCountCardByTag("test")).isEqualTo(3);
        workspacePage.filteringByTag("test");
        soft.assertThat(workspacePage.getCountCardByTag("test")).isEqualTo(3);
        soft.assertAll();
    }

    @Test
    public void checkFilteringByCardName() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992");
        soft.assertThat(workspacePage.getCountCardByTitle("Card from IDE")).isEqualTo(3);
        workspacePage.filteringByTitle("Card from IDE");
        soft.assertThat(workspacePage.getCountCardByTitle("Card from IDE")).isEqualTo(3);
        soft.assertAll();
    }

    @Test
    public void checkSearchByCardName() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992");
        soft.assertThat(workspacePage.getCountCardByTitle("Card from IDE")).isEqualTo(3);
        workspacePage.searchByText("Card from IDE", "Workspace from IDE");
        soft.assertThat(workspacePage.getNumberOfFoundCards()).isEqualTo(3);
        soft.assertAll();
    }

    @Test
    public void checkCreatingCustomFilter() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992")
                .filteringByTag("test")
                .filteringByTitle("Card from IDE")
                .saveCustomFilter("Custom");
        assertThat(workspacePage.getCustomFilters().contains("Custom")).isTrue();
    }

    @Test
    public void checkSearchByCustomFilter() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992");
        soft.assertThat(workspacePage.getCountCardByTitleAndTag("Card from IDE", "Feature")).isEqualTo(2);
        workspacePage
                .filteringByTag("Feature")
                .filteringByTitle("Card from IDE")
                .saveCustomFilter("MyFilter2")
                .clearFilters()
                .chooseCustomFilter("MyFilter2");
        soft.assertThat(workspacePage.getCountCards()).isEqualTo(2);
        soft.assertAll();
    }

    @Test
    public void checkOpenCard() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened()
                .openWorkspace("624992")
                .openCardByIndex(5);
        assertThat(workspacePage.isCardModalOpened()).isTrue();
    }

    @Test
    public void checkDragAndDropCard() {
        SoftAssertions soft = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .openWorkspace("624992");
        soft.assertThat(workspacePage.getCountCardsInColumn(0)).isEqualTo(0);
        workspacePage
                .dragAndDropCardToColumn(1, 0);
        soft.assertThat(workspacePage.getCountCardsInColumn(0)).isEqualTo(1);
        soft.assertAll();
    }

    @Test
    public void checkAddComment() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .openWorkspace("624992")
                .openCardByIndex(0)
                .cardIsOpened()
                .addComment("Hello from IDE")
                .addFileInComment(pic);

    }
}
