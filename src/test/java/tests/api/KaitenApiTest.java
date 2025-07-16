package tests.api;

import api.kaiten.dto.request.*;
import api.kaiten.dto.request.Column;
import api.kaiten.dto.request.Lane;
import api.kaiten.dto.response.*;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static api.kaiten.KaitenService.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class KaitenApiTest extends BaseApiTest {

    String
            workspaceName = "Workspace from IDE",
            boardName = "Board from IDE",
            cardName = "Card from IDE",
            cardDescription = "Random text in card comments. Check this text.",
            url = "www.google.com",
            urlDescription = "GOOGLE",
            cardTag = "Feature",
            cardChecklist = "Chose something";

    @Test
    public void checkCreatingWorkspace() {
        CreateWorkspaceRq rq = CreateWorkspaceRq.builder()
                .title(workspaceName)
                .build();

        CreateWorkspaceRs rs = createNewWorkspace(rq, token, workspace);

        assertThat(rs.title).contains(workspaceName);
    }

    @Test(priority = 1)
    public void checkCreatingBoard() {
        SoftAssertions soft = new SoftAssertions();

        List<Column> columns = kaiten.createColumnFirstType("1", "2", "3");
        List<Lane> lanes = kaiten.createLanes("");

        CreateBoardRq rq = CreateBoardRq.builder()
                .title(boardName)
                .columns(columns)
                .lanes(lanes)
                .build();
        CreateBoardRs rs = createNewBoard(rq, token, workspace, workspaceName);

        soft.assertThat(rs.title).contains(boardName);
        soft.assertThat(rs.columns.get(0).title).contains("1");
        soft.assertAll();

    }

    @Test(priority = 2)
    public void checkCreatingCard() {
        int boardId = getBoardId(token, workspace, workspaceName, boardName);
        CreateCardRq rq = CreateCardRq.builder()
                .boardId(boardId)
                .title(cardName)
                .build();
        CreateCardRs rs = createNewCard(rq, token, workspace);
        assertThat(rs.title).isEqualTo(cardName);
    }

    @Test(priority = 3)
    public void checkAddCommentToCard() {
        int cardId = getCardId(token, workspace, cardName);
        AddCardCommentRq rq = AddCardCommentRq.builder()
                .text(cardDescription)
                .build();
        AddCardCommentRs rs = addCommentToCard(rq, token, workspace, cardId);

        assertThat(rs.text).isEqualTo(cardDescription);
    }

    @Test(priority = 4)
    public void checkAddCommentWithFile() {
        File pic = new File("src/test/resources/images/photo.jpg");
        int cardId = getCardId(token, workspace, cardName);
        AddCardCommentRq rq = AddCardCommentRq.builder()
                .text(cardDescription)
                .build();
        AddCardCommentRs rs = addCommentWithFile(rq, token, workspace, cardId, pic);

        assertThat(rs.text).isEqualTo(cardDescription);
    }

    @Test(priority = 5)
    public void checkAddCardDescriptionAndAsap() {
        SoftAssertions soft = new SoftAssertions();

        int cardId = getCardId(token, workspace, cardName);
        UpdateCardRq rq = UpdateCardRq.builder()
                .description(cardDescription)
                .asap(true)
                .build();
        UpdateCardRs rs = addCardDescriptionAndAsap(rq, token, workspace, cardId);

        soft.assertThat(rs.description).isEqualTo(cardDescription);
        soft.assertThat(rs.asap).isTrue();
        soft.assertAll();
    }

    @Test(priority = 6)
    public void checkAddLinkToCard() {
        SoftAssertions soft = new SoftAssertions();

        int cardId = getCardId(token, workspace, cardName);
        System.out.println(cardId);
        AddCardLinkRq rq = AddCardLinkRq.builder()
                .url(url)
                .description(urlDescription)
                .build();
        AddCardLinkRs rs = addLinkToCard(rq, token, workspace, cardId);

        soft.assertThat(rs.description).isEqualTo(urlDescription);
        soft.assertThat(rs.url).isEqualTo(url);
        soft.assertAll();
    }

    @Test(priority = 7)
    public void checkAddTagToCard() {
        int cardId = getCardId(token, workspace, cardName);
        AddCardTagsRq rq = AddCardTagsRq.builder()
                .name(cardTag)
                .build();
        AddCardTagsRs rs = addTagToCard(rq, token, workspace, cardId);
        assertThat(rs.name).isEqualTo(cardTag);
    }

    @Test(priority = 8)
    public void checkAddCardCover() {
        File pic = new File("src/test/resources/images/3.jpg");
        int cardId = getCardId(token, workspace, cardName);
        AddCardCoverRq rq = AddCardCoverRq.builder()
                .cover(true)
                .build();
        addCoverToCard(rq, token, workspace, cardId, pic);
    }

    @Test(priority = 9)
    public void checkAddCardChecklist() {
        SoftAssertions soft = new SoftAssertions();
        int cardId = getCardId(token, workspace, cardName);
        AddCardChecklistRq rq = AddCardChecklistRq.builder()
                .name(cardChecklist)
                .position(0.1F)
                .build();
        AddCardChecklistRs rs = addChecklistToCard(rq, token, workspace, cardId);

        int checklistId = rs.id;

        AddCardChecklistItemRq firstItemRq = AddCardChecklistItemRq.builder()
                .text("First option")
                .position(2)
                .checked(false)
                .build();
        AddCardChecklistItemRs firstItemRs = addChecklistItemToCard(firstItemRq, token, workspace, cardId, checklistId);

        AddCardChecklistItemRq secondItemRq = AddCardChecklistItemRq.builder()
                .text("Second option")
                .position(2)
                .checked(false)
                .build();
        AddCardChecklistItemRs secondItemRs = addChecklistItemToCard(secondItemRq, token, workspace, cardId, checklistId);

        soft.assertThat(rs.name).isEqualTo(cardChecklist);
        soft.assertThat(firstItemRs.text).isEqualTo("First option");
        soft.assertThat(secondItemRs.text).isEqualTo("Second option");
        soft.assertAll();
    }

    @Test(priority = 10)
    public void checkDeletingCard() {
        int cardId = getCardId(token, workspace, cardName);
        DeleteCardRs rs = deleteCardById(token, workspace, cardId);

        assertThat(rs.condition).isEqualTo(3);
    }
}
