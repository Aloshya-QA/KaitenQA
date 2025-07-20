package api.kaiten.client;

import api.kaiten.dto.request.CreatePasswordRq;
import api.kaiten.dto.response.CreatePasswordRs;
import api.kaiten.dto.response.GetCurrentUserDataRs;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static java.lang.String.format;

@Log4j2
public class UserClient extends BaseClient {

    @Step("Getting current user data...")
    public GetCurrentUserDataRs getCurrentUser(String token, String workspace) {
        log.info("[API] Getting current user data...");
        return baseRequest(token)
                .when()
                .get(format("https://%s.kaiten.ru/api/latest/users/current", workspace))
                .then()
                .statusCode(200)
                .extract()
                .as(GetCurrentUserDataRs.class);
    }

    @Step("Creating password for user: ID{userId}...")
    public CreatePasswordRs createPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        log.info("[API] Creating password for user: ID{}...", userId);
        return baseRequest(token)
                .body(gsonRq.toJson(rq))
                .when()
                .patch(format("https://%s.kaiten.ru/api/latest/users/%s", workspace, userId))
                .then()
                .statusCode(200)
                .extract()
                .as(CreatePasswordRs.class);
    }
}
