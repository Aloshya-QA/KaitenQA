package api.kaiten;

import api.kaiten.dto.request.*;
import api.kaiten.dto.request.Column;
import api.kaiten.dto.request.Lane;
import api.kaiten.dto.response.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static api.kaiten.KaitenClient.*;

public class KaitenService {


    public static String getCurrentUserId(String token, String workspace) {
        return getCurrentUserData(token, workspace).id;
    }

    public static CreatePasswordRs setPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return createPassword(rq, userId, token, workspace);
    }

    public static CreateWorkspaceRs createNewWorkspace(CreateWorkspaceRq rq, String token, String workspace) {
        return createWorkspace(rq, token, workspace);
    }

    public static int getWorkspaceId(String token, String workspace, String workspaceName) {
        return getWorkspaces(token, workspace).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(workspaceName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Workspace with title '" + workspaceName + "' not found"));
    }

    public static CreateBoardRs createNewBoard(CreateBoardRq rq, String token, String workspace, String workspaceName) {
        int id = getWorkspaceId(token, workspace, workspaceName);
        return createBoard(rq, token, workspace, id);
    }

    public List<Column> createColumnFirstType(String... titles) {
        List<Column> columns = new ArrayList<>();
        for (String title : titles) {
            columns.add(Column.builder()
                    .title(title)
                    .type(1)
                    .build());
        }
        return columns;
    }

    public List<Lane> createLanes(String... titles) {
        List<Lane> lanes = new ArrayList<>();
        for (String title : titles) {
            lanes.add(Lane.builder()
                    .title(title)
                    .build());
        }

        return lanes;
    }

    public static int getBoardId(String token, String workspace, String workspaceName, String boardName) {
        int workspaceId = getWorkspaceId(token, workspace, workspaceName);
        return getBoards(token, workspace, workspaceId).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(boardName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Board with title '" + workspaceName + "' not found"));
    }

    public static CreateCardRs createNewCard(CreateCardRq rq, String token, String workspace) {
        return createCard(rq, token, workspace);
    }

    public static int getCardId(String token, String workspace, String cardName) {
        return getCards(token, workspace).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(cardName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Board with title '" + cardName + "' not found"));
    }

    public static AddCardCommentRs addCommentToCard(AddCardCommentRq rq, String token, String workspace, int cardId) {
        return addCardComment(rq, token, workspace, cardId);
    }

    public static AddCardCommentRs addCommentWithFile(AddCardCommentRq rq, String token, String workspace, int cardId, File file) {
        return addCardComment(rq, token, workspace, cardId, file);
    }

    public static UpdateCardRs addCardDescriptionAndAsap(UpdateCardRq rq, String token, String workspace, int cardId) {
        return updateCard(rq, token, workspace, cardId);
    }

    public static DeleteCardRs deleteCardById(String token, String workspace, int cardId) {
        return deleteCard(token, workspace, cardId);
    }

    public static AddCardLinkRs addLinkToCard(AddCardLinkRq rq, String token, String workspace, int cardId) {
        return updateCard(rq, token, workspace, cardId);
    }

    public static AddCardTagsRs addTagToCard(AddCardTagsRq rq, String token, String workspace, int cardId) {
        return addCardTags(rq, token, workspace, cardId);
    }

    public static AddCardCoverRs addCoverToCard(AddCardCoverRq rq, String token, String workspace, int cardId, File file) {
        AddCardFileRs rs = addCardFile(token, workspace, cardId, file);
        System.out.println("ID image: " + rs.id);
        return addCardCover(rq, token, workspace, cardId, rs.id);
    }

    public static AddCardChecklistRs addChecklistToCard(AddCardChecklistRq rq, String token, String workspace, int cardId) {
        return addCardChecklist(rq, token, workspace, cardId);
    }

    public static AddCardChecklistItemRs addChecklistItemToCard(AddCardChecklistItemRq rq, String token, String workspace, int cardId, int checklistId) {
        return addCardChecklistItem(rq, token, workspace, cardId, checklistId);
    }
}
