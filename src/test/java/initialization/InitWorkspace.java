package initialization;

import api.kaiten.KaitenService;
import api.kaiten.dto.request.*;
import api.kaiten.dto.response.CreateBoardRs;
import api.kaiten.dto.response.CreateCardRs;
import factory.DataFactory;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static api.kaiten.KaitenService.*;
import static java.lang.String.format;
import static utils.PropertyReader.*;

@Log4j2
public class InitWorkspace {

    static DataFactory data = new DataFactory();

    private static final String
            BOARD_NAME = data.generateBoardName(),
            WORKSPACE_NAME = data.generateSpaceName(),
            TOKEN = getProperty("kaitenApiToken"),
            WORKSPACE = getProperty("workspace");

    private static int
            firstColumnId,
            secondColumnId,
            thirdColumnId,
            cardCounter;

    public static void creatingWorkspace() throws InterruptedException {
        log.info("Start creating workspace: '{}'...", WORKSPACE_NAME);
        CreateWorkspaceRq rq = CreateWorkspaceRq.builder()
                .title(WORKSPACE_NAME)
                .build();

        int workspaceId = KaitenService.createNewWorkspace(rq, TOKEN, WORKSPACE).id;
        setProperty("workspaceName", WORKSPACE_NAME);
        setProperty("workspaceId", String.valueOf(workspaceId));
        saveProperties();
        log.info("Workspace is created");
        Thread.sleep(1000);
    }

    public static void creatingBoard() throws InterruptedException {
        log.info("Start creating board: '{}'...", BOARD_NAME);
        List<Column> columns = List.of(
                KaitenService.createColumnByType(1, "Очередь"),
                KaitenService.createColumnByType(2, "В работе"),
                KaitenService.createColumnByType(3, "Готово")
        );
        List<Lane> lanes = KaitenService.createLanes("");

        CreateBoardRq rq = CreateBoardRq.builder()
                .title(BOARD_NAME)
                .columns(columns)
                .lanes(lanes)
                .build();
        CreateBoardRs rs = KaitenService.createNewBoard(rq, TOKEN, WORKSPACE, WORKSPACE_NAME);

        firstColumnId = rs.columns.get(0).id;
        secondColumnId = rs.columns.get(1).id;
        thirdColumnId = rs.columns.get(2).id;

        setProperty("boardName", BOARD_NAME);
        setProperty("firstColumnId", String.valueOf(rs.columns.get(0).id));
        saveProperties();
        log.info("Board is created");
        Thread.sleep(1000);
    }

    public static void creatingCards() throws InterruptedException {
        int boardId = KaitenService.getBoardId(TOKEN, WORKSPACE, WORKSPACE_NAME, BOARD_NAME);
        createCardInColumn(3, firstColumnId, boardId, 2);
        Thread.sleep(1000);
        createCardInColumn(2, secondColumnId, boardId, 0);
        Thread.sleep(1000);
        createCardInColumn(1, thirdColumnId, boardId, 0);
        Thread.sleep(1000);
        updateCards();
    }

    private static void createCardInColumn(int countCard, int columnId, int boardId, int duplicateCount) throws InterruptedException {
        log.info("Start creating {} cards in column #{}", countCard, columnId);
        String duplicateCardName = data.generateCardName();

        for (int i = 0; i < countCard; i++) {
            String cardName;

            if (i < duplicateCount) {
                cardName = duplicateCardName;
            } else {
                cardName = data.generateCardName();
            }

            CreateCardRq rq = CreateCardRq.builder()
                    .boardId(boardId)
                    .columnId(columnId)
                    .title(cardName)
                    .build();
            CreateCardRs rs = KaitenService.createNewCard(rq, TOKEN, WORKSPACE);

            if (cardCounter == 0) {
                setProperty("card_" + cardCounter + "_name", cardName);
            }

            log.info("Card #{} created", i);
            setProperty("card_" + cardCounter + "_id", String.valueOf(rs.id));
            cardCounter++;
            Thread.sleep(1000);
        }

        saveProperties();
    }

    private static void updateCards() throws InterruptedException {
        log.info("Start adding info to cards...");
        List<Integer> cardsId = new ArrayList<>();

        for (int i = 0; i < cardCounter; i++) {
            String idString = getProperty("card_" + i + "_id");
            if (idString != null && !idString.isBlank()) {
                try {
                    cardsId.add(Integer.parseInt(idString));
                } catch (NumberFormatException e) {
                    log.error("Invalid card id format: " + idString);
                }
            }
        }

        String sharedTag = data.generateSecondTag();

        for (int i = 0; i < cardsId.size(); i++) {
            int cardId = cardsId.get(i);

            int picNumber = (int) (Math.random() * 6) + 1;
            boolean randomBool = Math.random() < 0.5;
            File pic = new File(format("src/test/resources/images/%s.jpg", picNumber));

            AddCardCommentRq commentRq = AddCardCommentRq.builder()
                    .text(data.generateDescription())
                    .build();
            addCommentWithFile(commentRq, TOKEN, WORKSPACE, cardId, pic);

            UpdateCardRq updateRq = UpdateCardRq.builder()
                    .description(data.generateDescription())
                    .asap(randomBool)
                    .build();
            addCardDescriptionAndAsap(updateRq, TOKEN, WORKSPACE, cardId);

            AddCardLinkRq linkRq = AddCardLinkRq.builder()
                    .url(data.generateUrl())
                    .description(data.generateUrlTitle())
                    .build();
            addLinkToCard(linkRq, TOKEN, WORKSPACE, cardId);

            if (i < 2) {
                AddCardTagsRq tagRq = AddCardTagsRq.builder()
                        .name(sharedTag)
                        .build();
                addTagToCard(tagRq, TOKEN, WORKSPACE, cardId);
                setProperty("card_tag", sharedTag);
            }

            if (picNumber > 4) {
                AddCardTagsRq tagRq = AddCardTagsRq.builder()
                        .name(data.generateTag())
                        .build();
                addTagToCard(tagRq, TOKEN, WORKSPACE, cardId);
            }

            if (picNumber > 5) {
                AddCardTagsRq tagRq = AddCardTagsRq.builder()
                        .name(data.generateTag())
                        .build();
                addTagToCard(tagRq, TOKEN, WORKSPACE, cardId);
            }

            if (picNumber <= 2) {
                AddCardTagsRq tagRq = AddCardTagsRq.builder()
                        .name(data.generateTag())
                        .build();
                addTagToCard(tagRq, TOKEN, WORKSPACE, cardId);

                AddCardCoverRq coverRq = AddCardCoverRq.builder()
                        .cover(true)
                        .build();
                addCoverToCard(coverRq, TOKEN, WORKSPACE, cardId, pic);
            }

            AddCardTagsRq randomTagRq = AddCardTagsRq.builder()
                    .name(data.generateTag())
                    .build();
            addTagToCard(randomTagRq, TOKEN, WORKSPACE, cardId);

            saveProperties();
            log.info("Card #{} is filled", i);
            Thread.sleep(1000);
        }
    }
}
