package api.kaiten;

import api.kaiten.client.BoardClient;
import api.kaiten.client.CardClient;
import api.kaiten.client.UserClient;
import api.kaiten.client.WorkspaceClient;
import api.kaiten.dto.request.*;
import api.kaiten.dto.request.Column;
import api.kaiten.dto.request.Lane;
import api.kaiten.dto.response.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KaitenService {

    private static final UserClient USER_CLIENT = new UserClient();
    private static final WorkspaceClient WORKSPACE_CLIENT = new WorkspaceClient();
    private static final BoardClient BOARD_CLIENT = new BoardClient();
    private static final CardClient CARD_CLIENT = new CardClient();

    public static String getCurrentUserId(String token, String workspace) {
        return USER_CLIENT.getCurrentUser(token, workspace).id;
    }

    public static CreatePasswordRs setPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return USER_CLIENT.createPassword(rq, userId, token, workspace);
    }

    public static CreateWorkspaceRs createNewWorkspace(CreateWorkspaceRq rq, String token, String workspace) {
        return WORKSPACE_CLIENT.createWorkspace(rq, token, workspace);
    }

    public static int getWorkspaceId(String token, String workspace, String workspaceName) {
        return WORKSPACE_CLIENT.getWorkspaces(token, workspace).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(workspaceName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Workspace with title '" + workspaceName + "' not found"));
    }

    public static DeleteWorkspaceRs deleteWorkspace(String token, String workspace) {
        return WORKSPACE_CLIENT.deleteWorkspace(token, workspace);
    }

    public static CreateBoardRs createNewBoard(CreateBoardRq rq, String token, String workspace, String workspaceName) {
        int id = getWorkspaceId(token, workspace, workspaceName);
        return BOARD_CLIENT.createBoard(rq, token, workspace, id);
    }

    public static Column createColumnByType(int type, String title) {
        return Column.builder()
                    .title(title)
                .type(type)
                .build();
    }

    public static List<Lane> createLanes(String... titles) {
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
        return BOARD_CLIENT.getBoards(token, workspace, workspaceId).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(boardName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Board with title '" + boardName + "' not found"));
    }

    public static CreateCardRs createNewCard(CreateCardRq rq, String token, String workspace) {
        return CARD_CLIENT.createCard(rq, token, workspace);
    }

    public static int getCardId(String token, String workspace, String cardName) {
        return CARD_CLIENT.getCards(token, workspace).stream()
                .filter(w -> w.title != null && w.title.equalsIgnoreCase(cardName))
                .findFirst()
                .map(w -> w.id)
                .orElseThrow(() -> new RuntimeException("Card with title '" + cardName + "' not found"));
    }

    public static AddCardCommentRs addCommentToCard(AddCardCommentRq rq, String token, String workspace, int cardId) {
        return CARD_CLIENT.addComment(rq, token, workspace, cardId);
    }

    public static AddCardCommentRs addCommentWithFile(AddCardCommentRq rq, String token, String workspace, int cardId, File file) {
        return CARD_CLIENT.addCommentWithFile(rq, token, workspace, cardId, file);
    }

    public static UpdateCardRs addCardDescriptionAndAsap(UpdateCardRq rq, String token, String workspace, int cardId) {
        return CARD_CLIENT.updateCard(rq, token, workspace, cardId);
    }

    public static DeleteCardRs deleteCardById(String token, String workspace, int cardId) {
        return CARD_CLIENT.deleteCard(token, workspace, cardId);
    }

    public static AddCardLinkRs addLinkToCard(AddCardLinkRq rq, String token, String workspace, int cardId) {
        return CARD_CLIENT.addLink(rq, token, workspace, cardId);
    }

    public static AddCardTagsRs addTagToCard(AddCardTagsRq rq, String token, String workspace, int cardId) {
        return CARD_CLIENT.addTag(rq, token, workspace, cardId);
    }

    public static AddCardCoverRs addCoverToCard(AddCardCoverRq rq, String token, String workspace, int cardId, File file) {
        AddCardFileRs rs = CARD_CLIENT.uploadFile(token, workspace, cardId, file);
        return CARD_CLIENT.addCover(rq, token, workspace, cardId, rs.id);
    }

    public static AddCardChecklistRs addChecklistToCard(AddCardChecklistRq rq, String token, String workspace, int cardId) {
        return CARD_CLIENT.addChecklist(rq, token, workspace, cardId);
    }

    public static AddCardChecklistItemRs addChecklistItem(AddCardChecklistItemRq rq, String token, String workspace, int cardId, int checklistId) {
        return CARD_CLIENT.addChecklistItem(rq, token, workspace, cardId, checklistId);
    }
}
