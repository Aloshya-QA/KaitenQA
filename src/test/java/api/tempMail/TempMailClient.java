package api.tempMail;

import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import api.tempMail.dto.request.CreateMailRq;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TempMailClient {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static GetDomainsRs getDomains() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.mail.tm/domains")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetDomainsRs.class);
    }

    public static CreateMailRs createTempMail(CreateMailRq rq) {
        return given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(rq))
                .when()
                .post("https://api.mail.tm/accounts")
                .then()
                .log().status()
                .statusCode(201)
                .extract()
                .as(CreateMailRs.class);
    }

    public static GetTokenRs getAuthToken(GetTokenRq rq) {
        return given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(rq))
                .when()
                .post("https://api.mail.tm/token")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetTokenRs.class);
    }

    public static List<GetMessagesRs> getMessages(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("https://api.mail.tm/messages")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", GetMessagesRs.class);
    }

    public static GetTextMessageRs getTextMessage(String messageId, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("https://api.mail.tm/messages/" + messageId)
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetTextMessageRs.class);
    }
}
