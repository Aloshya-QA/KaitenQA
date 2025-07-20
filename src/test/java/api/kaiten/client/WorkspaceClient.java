package api.kaiten.client;

import api.kaiten.dto.request.CreateWorkspaceRq;
import api.kaiten.dto.response.CreateWorkspaceRs;
import api.kaiten.dto.response.DeleteWorkspaceRs;
import api.kaiten.dto.response.GetWorkspacesRs;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static java.lang.String.format;

@Log4j2
public class WorkspaceClient extends BaseClient {

    @Step("Creating workspace...")
    public CreateWorkspaceRs createWorkspace(CreateWorkspaceRq rq, String token, String workspace) {
        log.info("[API] Creating workspace...");
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .post(format("https://%s.kaiten.ru/api/latest/spaces", workspace))
                .then()
                .statusCode(200)
                .extract()
                .as(CreateWorkspaceRs.class);
    }

    @Step("Getting workspaces data...")
    public List<GetWorkspacesRs> getWorkspaces(String token, String workspace) {
        log.info("[API] Getting workspaces data...");
        return List.of(baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/spaces", workspace))
                .then()
                .statusCode(200)
                .extract()
                .as(GetWorkspacesRs[].class));
    }

    @Step("Deleting workspace")
    public DeleteWorkspaceRs deleteWorkspace(String token, String workspace) {
        log.info("[API] Deleting workspace");
        return baseRequest(token)
                .when()
                .delete(format("https://%s.kaiten.ru/api/latest/spaces", workspace))
                .then()
                .statusCode(200)
                .extract()
                .as(DeleteWorkspaceRs.class);
    }
}
