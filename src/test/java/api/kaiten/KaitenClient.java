package api.kaiten;

import api.kaiten.dto.request.*;
import api.kaiten.dto.response.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class KaitenClient {


    static Gson gsonRq = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    static RestAssuredConfig gsonRs = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig()
                .defaultObjectMapperType(ObjectMapperType.GSON)
    );

    private static RequestSpecification baseRequest(String token) {
        return given()
                .config(gsonRs)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", format("Bearer %s",token));
    }


    public static CreatePasswordRs createPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .patch("https://" + workspace + ".kaiten.ru/api/latest/users/" + userId)
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(CreatePasswordRs.class);
    }

    public static GetCurrentUserDataRs getCurrentUserData(String token, String workspace) {
        return baseRequest(token)
                .when()
                .get("https://" + workspace + ".kaiten.ru/api/latest/users/current")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetCurrentUserDataRs.class);
    }

    public static CreateWorkspaceRs createWorkspace(CreateWorkspaceRq rq, String token, String workspace) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/spaces", workspace))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(CreateWorkspaceRs.class);
    }

    public static List<GetWorkspacesRs> getWorkspaces(String token, String workspace) {
        return List.of(baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/spaces", workspace))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetWorkspacesRs[].class));
    }

    public static CreateBoardRs createBoard(CreateBoardRq rq, String token, String workspace, int workspaceId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/spaces/%s/boards", workspace, workspaceId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(CreateBoardRs.class);
    }

    public static List<GetBoardsRs> getBoards(String token, String workspace, int workspaceId) {
        return List.of(baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/spaces/%s/boards", workspace, workspaceId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetBoardsRs[].class));
    }

    public static CreateCardRs createCard(CreateCardRq rq, String token, String workspace) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards", workspace))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(CreateCardRs.class);
    }

    public static List<GetCardsRs> getCards(String token, String workspace) {
        return List.of(baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/cards", workspace))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetCardsRs[].class));
    }

    public static AddCardCommentRs addCardComment(AddCardCommentRq rq, String token, String workspace, int cardId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/comments", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardCommentRs.class);
    }

    public static AddCardCommentRs addCardComment(AddCardCommentRq rq, String token, String workspace, int cardId, File file) {
        return given()
                .config(gsonRs)
                .header("Authorization", format("Bearer %s",token))
                .multiPart("text", rq.getText())
                .multiPart("files[]", file)
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/comments", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardCommentRs.class);
    }

    public static UpdateCardRs updateCard(UpdateCardRq rq, String token, String workspace, int cardId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .patch(format("https://%s.kaiten.ru/api/latest/cards/%s", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(UpdateCardRs.class);
    }

    public static DeleteCardRs deleteCard(String token, String workspace, int cardId) {
        return baseRequest(token)
                .when()
                .delete(format("https://%s.kaiten.ru/api/latest/cards/%s", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(DeleteCardRs.class);
    }

    public static AddCardLinkRs updateCard(AddCardLinkRq rq, String token, String workspace, int cardId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/external-links", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardLinkRs.class);
    }

    public static AddCardTagsRs addCardTags(AddCardTagsRq rq, String token, String workspace, int cardId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/tags", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardTagsRs.class);
    }

    public static AddCardFileRs addCardFile(String token, String workspace, int cardId, File file) {
        return given()
                .config(gsonRs)
                .header("Authorization", format("Bearer %s",token))
                .multiPart("file", file)
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/files", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardFileRs.class);
    }

    public static AddCardCoverRs addCardCover(AddCardCoverRq rq, String token, String workspace, int cardId, int fileId) {
        return given()
                .config(gsonRs)
                .contentType(ContentType.JSON)
                .header("Authorization", format("Bearer %s",token))
                .body(gsonRq.toJson(rq))
                .when()
                .patch(format("https://%s.kaiten.ru/api/latest/cards/%s/files/%s", workspace, cardId, fileId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardCoverRs.class);
    }

    public static AddCardChecklistRs addCardChecklist(AddCardChecklistRq rq, String token, String workspace, int cardId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/checklists", workspace, cardId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardChecklistRs.class);
    }

    public static AddCardChecklistItemRs addCardChecklistItem(AddCardChecklistItemRq rq, String token, String workspace, int cardId, int checklistId) {
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/cards/%s/checklists/%s/items", workspace, cardId, checklistId))
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(AddCardChecklistItemRs.class);
    }
}
