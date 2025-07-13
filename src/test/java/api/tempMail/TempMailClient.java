package api.tempMail;

import api.tempMail.dto.request.GetTokenRq;
import api.tempMail.dto.response.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import api.tempMail.dto.request.CreateMailRq;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class TempMailClient {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    static RestAssuredConfig gsonRs = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig()
                    .defaultObjectMapperType(ObjectMapperType.GSON)
    );

    private static RequestSpecification baseRequest() {
        return given()
                .config(gsonRs)
                .contentType(ContentType.JSON);
    }

    public static GetDomainsRs getDomains() {
        return baseRequest()
                .when()
                .get("https://api.mail.tm/domains")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetDomainsRs.class);
    }

    public static CreateMailRs createTempMail(CreateMailRq rq) {
        return baseRequest()
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
        return baseRequest()
                .body(gson.toJson(rq))
                .when()
                .post("https://api.mail.tm/token")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetTokenRs.class);
    }

    public static GetMessagesRs getMessages(String token) {
        return baseRequest()
                .header("Authorization", format("Bearer %s", token))
                .when()
                .get("https://api.mail.tm/messages")
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetMessagesRs.class);
    }

    public static GetTextMessageRs getTextMessage(String messageId, String token) {
        return baseRequest()
                .accept(ContentType.JSON)
                .header("Authorization", format("Bearer %s", token))
                .when()
                .get("https://api.mail.tm/messages/" + messageId)
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .as(GetTextMessageRs.class);
    }
}
