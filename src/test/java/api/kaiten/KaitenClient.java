package api.kaiten;

import api.kaiten.dto.request.CreatePasswordRq;
import api.kaiten.dto.responce.CreatePasswordRs;
import api.kaiten.dto.responce.GetCurrentUserDataRs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class KaitenClient {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static CreatePasswordRs createPassword(CreatePasswordRq rq, String userId, String token, String workspace) {
        return given()
                .log().all()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(gson.toJson(rq))
                .when()
                .patch("https://" + workspace + ".kaiten.ru/api/latest/users/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(CreatePasswordRs.class);
    }
    public static GetCurrentUserDataRs getCurrentUserData(String token, String workspace) {
        return given()
                .log().all()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("https://" + workspace + ".kaiten.ru/api/latest/users/current")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(GetCurrentUserDataRs.class);
    }


}
