package api.kaiten.client;

import api.kaiten.dto.request.CreateBoardRq;
import api.kaiten.dto.response.CreateBoardRs;
import api.kaiten.dto.response.GetBoardsRs;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static java.lang.String.format;

@Log4j2
public class BoardClient extends BaseClient {

    @Step("Creating board...")
    public CreateBoardRs createBoard(CreateBoardRq rq, String token, String workspace, int workspaceId) {
        log.info("[API] Creating board...");
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/spaces/%s/boards", workspace, workspaceId))
                .then()
                .statusCode(200)
                .extract()
                .as(CreateBoardRs.class);
    }

    @Step("Getting boards data...")
    public List<GetBoardsRs> getBoards(String token, String workspace, int workspaceId) {
        log.info("[API] Getting boards data...");
        return List.of(baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/spaces/%s/boards", workspace, workspaceId))
                .then()
                .statusCode(200)
                .extract()
                .as(GetBoardsRs[].class));
    }
}
