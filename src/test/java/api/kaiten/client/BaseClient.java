package api.kaiten.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;

public class BaseClient {

    static Gson gsonRq = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    static RestAssuredConfig gsonRs = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig()
                    .defaultObjectMapperType(ObjectMapperType.GSON)
    );

    static RequestSpecification baseRequest(String token) {
        return given()
                .config(gsonRs)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", format("Bearer %s", token));
    }
}
