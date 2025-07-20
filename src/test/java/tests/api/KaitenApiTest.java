package tests.api;

import api.kaiten.KaitenService;
import api.kaiten.dto.request.*;
import api.kaiten.dto.request.Column;
import api.kaiten.dto.request.Lane;
import api.kaiten.dto.response.*;
import factory.DataFactory;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static api.kaiten.KaitenService.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class KaitenApiTest extends BaseApiTest {

    DataFactory data = new DataFactory();
    File pic = new File("src/test/resources/images/3.jpg");

    private final String
            spaceName = data.generateSpaceName(),
            genBoardName = data.generateBoardName(),
            generatedCardName = data.generateCardName(),
            cardDescription = data.generateDescription(),
            firstColumnName = data.generateColumnName(),
            secondColumnName = data.generateColumnName(),
            thirdColumnName = data.generateColumnName(),
            checklistName = data.generateChecklist(),
            checklistFirstItem = data.generateChecklistItem(),
            checklistSecondItem = data.generateChecklistItem(),
            url = data.generateUrl(),
            urlTitle = data.generateUrlTitle(),
            tag = data.generateTag();

    @Test(
            testName = "Check creating workspace",
            groups = {"Regression"},
            priority = 1
    )
    public void checkCreatingWorkspace() {
        CreateWorkspaceRq rq = CreateWorkspaceRq.builder()
                .title(spaceName)
                .build();
        CreateWorkspaceRs rs = createNewWorkspace(rq, token, workspace);
        assertThat(rs.title).contains(spaceName);
    }

    @Test(
            testName = "Check creating board",
            groups = {"Regression"},
            priority = 2
    )
    public void checkCreatingBoard() {
        List<Column> columns = List.of(
                createColumnByType(1, firstColumnName),
                createColumnByType(2, secondColumnName),
                createColumnByType(3, thirdColumnName)
        );
        List<Lane> lanes = KaitenService.createLanes("");

        CreateBoardRq rq = CreateBoardRq.builder()
                .title(genBoardName)
                .columns(columns)
                .lanes(lanes)
                .build();
        CreateBoardRs rs = createNewBoard(rq, token, workspace, workspaceName);

        soft.assertThat(rs.title).contains(genBoardName);
        soft.assertThat(rs.columns.get(0).title).contains(firstColumnName);
        soft.assertThat(rs.columns.get(1).title).contains(secondColumnName);
        soft.assertThat(rs.columns.get(2).title).contains(thirdColumnName);
        soft.assertAll();
    }

    @Test(
            testName = "Check creating card",
            groups = {"Regression"},
            priority = 3
    )
    public void checkCreatingCard() {
        int boardId = getBoardId(token, workspace, workspaceName, boardName);
        CreateCardRq rq = CreateCardRq.builder()
                .boardId(boardId)
                .columnId(firstColumnId)
                .title(generatedCardName)
                .build();
        CreateCardRs rs = createNewCard(rq, token, workspace);
        assertThat(rs.title).isEqualTo(generatedCardName);
    }

    @Test(
            testName = "Check adding comment to card",
            groups = {"Regression"},
            priority = 4
    )
    public void checkAddCommentToCard() {
        AddCardCommentRq rq = AddCardCommentRq.builder()
                .text(cardDescription)
                .build();
        AddCardCommentRs rs = addCommentToCard(rq, token, workspace, firstCardId);

        assertThat(rs.text).isEqualTo(cardDescription);
    }

    @Test(
            testName = "Check adding comment with file to card",
            groups = {"Regression"},
            priority = 5
    )
    public void checkAddCommentWithFile() {
        AddCardCommentRq rq = AddCardCommentRq.builder()
                .text(cardDescription)
                .build();
        AddCardCommentRs rs = addCommentWithFile(rq, token, workspace, firstCardId, pic);

        assertThat(rs.text).isEqualTo(cardDescription);
    }

    @Test(
            testName = "Check adding description and asap to card",
            groups = {"Regression"},
            priority = 6
    )
    public void checkAddCardDescriptionAndAsap() {
        UpdateCardRq rq = UpdateCardRq.builder()
                .description(cardDescription)
                .asap(true)
                .build();
        UpdateCardRs rs = addCardDescriptionAndAsap(rq, token, workspace, firstCardId);

        soft.assertThat(rs.description).isEqualTo(cardDescription);
        soft.assertThat(rs.asap).isTrue();
        soft.assertAll();
    }

    @Test(
            testName = "Check adding link to card",
            groups = {"Regression"},
            priority = 7
    )
    public void checkAddLinkToCard() {
        AddCardLinkRq rq = AddCardLinkRq.builder()
                .url(url)
                .description(urlTitle)
                .build();
        AddCardLinkRs rs = addLinkToCard(rq, token, workspace, firstCardId);

        soft.assertThat(rs.description).isEqualTo(urlTitle);
        soft.assertThat(rs.url).isEqualTo(url);
        soft.assertAll();
    }

    @Test(
            testName = "Check adding tag to card",
            groups = {"Regression"},
            priority = 8
    )
    public void checkAddTagToCard() {
        AddCardTagsRq rq = AddCardTagsRq.builder()
                .name(tag)
                .build();
        AddCardTagsRs rs = addTagToCard(rq, token, workspace, firstCardId);
        assertThat(rs.name).isEqualTo(tag);
    }

    @Test(
            testName = "Check adding checklist to card",
            groups = {"Regression"},
            priority = 9
    )
    public void checkAddCardChecklist() {
        AddCardChecklistRq rq = AddCardChecklistRq.builder()
                .name(checklistName)
                .build();
        AddCardChecklistRs rs = addChecklistToCard(rq, token, workspace, firstCardId);

        AddCardChecklistItemRq firstItemRq = AddCardChecklistItemRq.builder()
                .text(checklistFirstItem)
                .checked(false)
                .build();
        AddCardChecklistItemRs firstItemRs = addChecklistItem(firstItemRq, token, workspace, firstCardId, rs.id);

        AddCardChecklistItemRq secondItemRq = AddCardChecklistItemRq.builder()
                .text(checklistSecondItem)
                .checked(false)
                .build();
        AddCardChecklistItemRs secondItemRs = addChecklistItem(secondItemRq, token, workspace, firstCardId, rs.id);

        soft.assertThat(rs.name).isEqualTo(checklistName);
        soft.assertThat(firstItemRs.text).isEqualTo(checklistFirstItem);
        soft.assertThat(secondItemRs.text).isEqualTo(checklistSecondItem);
        soft.assertAll();
    }

    @Test(
            testName = "Check deleting card",
            groups = {"Regression"},
            priority = 10
    )
    public void checkDeletingCard() {
        int boardId = getBoardId(token, workspace, workspaceName, boardName);
        CreateCardRq rq = CreateCardRq.builder()
                .boardId(boardId)
                .columnId(firstColumnId)
                .title(generatedCardName)
                .build();
        int cardId = createNewCard(rq, token, workspace).id;
        DeleteCardRs rs = deleteCardById(token, workspace, cardId);

        assertThat(rs.condition).isEqualTo(3);
    }
}
