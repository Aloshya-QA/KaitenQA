package api.kaiten.client;

import api.kaiten.dto.request.*;
import api.kaiten.dto.response.*;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

@Slf4j
public class CardClient extends BaseClient {

    @Step("Creating card...")
    public CreateCardRs createCard(CreateCardRq rq, String token, String workspace) {
        log.info("[API] Creating card...");
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards", workspace))
                .then()
                .statusCode(200)
                .extract()
                .as(CreateCardRs.class);
    }

    @Step("Getting list card...")
    public List<GetCardsRs> getCards(String token, String workspace) {
        log.info("[API] Getting list card...");
        return List.of(given()
                .config(gsonRs)
                .accept(ContentType.JSON)
                .header("Authorization", format("Bearer %s", token))
                .when()
                .get("https://" + workspace + ".kaiten.ru/api/latest/cards")
                .then()
                .statusCode(200)
                .extract()
                .as(GetCardsRs[].class));
    }

    @Step("Adding comment to card: ID{cardId}...")
    public AddCardCommentRs addComment(AddCardCommentRq rq, String token, String workspace, int cardId) {
        log.info("[API] Adding comment to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/comments", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardCommentRs.class);
    }

    @Step("Adding comment with file to card...")
    public AddCardCommentRs addCommentWithFile(AddCardCommentRq rq, String token, String workspace, int cardId, File file) {
        log.info("[API] Adding comment with file to card: ID{}...", cardId);
        return given()
                .config(gsonRs)
                .accept(ContentType.JSON)
                .header("Authorization", format("Bearer %s", token))
                .multiPart("text", rq.getText())
                .multiPart("files[]", file)
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/comments", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardCommentRs.class);
    }

    @Step("Updating card: ID{cardID}...")
    public UpdateCardRs updateCard(UpdateCardRq rq, String token, String workspace, int cardId) {
        log.info("[API] Updating card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .patch(format("https://%s.kaiten.ru/api/latest/cards/%s", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(UpdateCardRs.class);
    }

    @Step("Deleting card: ID{cardId}...")
    public DeleteCardRs deleteCard(String token, String workspace, int cardId) {
        log.info("[API] Deleting card: ID{}...", cardId);
        return given()
                .config(gsonRs)
                .accept(ContentType.JSON)
                .header("Authorization", format("Bearer %s", token))
                .when()
                .delete(format("https://%s.kaiten.ru/api/latest/cards/%s", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(DeleteCardRs.class);
    }

    @Step("Adding link to card: ID{cardId}...")
    public AddCardLinkRs addLink(AddCardLinkRq rq, String token, String workspace, int cardId) {
        log.info("[API] Adding link to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/external-links", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardLinkRs.class);
    }

    @Step("Adding tag to card: ID{cardId}...")
    public AddCardTagsRs addTag(AddCardTagsRq rq, String token, String workspace, int cardId) {
        log.info("[API] Adding tag to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/tags", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardTagsRs.class);
    }

    @Step("Adding upload file to card: ID{cardId}...")
    public AddCardFileRs uploadFile(String token, String workspace, int cardId, File file) {
        log.info("[API] Adding upload file to card: ID{}...", cardId);
        return given()
                .config(gsonRs)
                .header("Authorization", format("Bearer %s", token))
                .multiPart("file", file)
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/files", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardFileRs.class);
    }

    @Step("Adding cover to card: ID{cardId}...")
    public AddCardCoverRs addCover(AddCardCoverRq rq, String token, String workspace, int cardId, int fileId) {
        log.info("[API] Adding cover to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .patch(format("https://%s.kaiten.ru/api/latest/cards/%s/files/%s", workspace, cardId, fileId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardCoverRs.class);
    }

    @Step("Adding checklist to card: ID{cardId}...")
    public AddCardChecklistRs addChecklist(AddCardChecklistRq rq, String token, String workspace, int cardId) {
        log.info("[API] Adding checklist to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/checklists", workspace, cardId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardChecklistRs.class);
    }

    @Step("Adding checklist items to card: ID{cardId}...")
    public AddCardChecklistItemRs addChecklistItem(AddCardChecklistItemRq rq, String token, String workspace, int cardId, int checklistId) {
        log.info("[API] Adding checklist items to card: ID{}...", cardId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/checklists/%s/items", workspace, cardId, checklistId))
                .then()
                .statusCode(200)
                .extract()
                .as(AddCardChecklistItemRs.class);
    }
}
