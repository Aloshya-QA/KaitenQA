package api.tempMail;

import api.tempMail.dto.request.TokenRq;
import api.tempMail.dto.response.MessagesRs;
import api.tempMail.dto.response.TextMessageRs;
import api.tempMail.dto.response.TokenRs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import api.tempMail.dto.request.CreateMailRq;
import api.tempMail.dto.response.CreateMailRs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TempMailClient {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static CreateMailRs createTempMail(CreateMailRq rq) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(gson.toJson(rq))
                .when()
                .post("https://api.mail.tm/accounts")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(CreateMailRs.class);
    }

    public static TokenRs getAuthToken(TokenRq rq) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(gson.toJson(rq))
                .when()
                .post("https://api.mail.tm/token")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(TokenRs.class);
    }

    public static List<MessagesRs> getMessages(String token) {
        return given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("https://api.mail.tm/messages")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", MessagesRs.class);
    }

    public static TextMessageRs getTextMessage(String messageId, String token) {
        return given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .when()
                .get("https://api.mail.tm/messages/" + messageId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(TextMessageRs.class);
    }


}
