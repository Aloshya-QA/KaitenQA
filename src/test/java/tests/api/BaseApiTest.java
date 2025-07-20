package tests.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeSuite;

import static initialization.Init.startInit;
import static utils.PropertyReader.getProperty;

public class BaseApiTest {

    String
            token,
            workspace,
            workspaceName,
            boardName,
            firstCardName;

    int
            firstColumnId,
            firstCardId;

    SoftAssertions soft;

    @BeforeSuite(alwaysRun = true)
    public void loadVariables() throws InterruptedException {
        startInit();
        RestAssured.filters(new AllureRestAssured());
        this.soft = new SoftAssertions();
        this.token = getProperty("kaitenApiToken");
        this.workspace = getProperty("workspace");
        this.workspaceName = getProperty("workspaceName");
        this.boardName = getProperty("boardName");
        this.firstCardName = getProperty("card_0_name");
        this.firstColumnId = Integer.parseInt(getProperty("firstColumnId"));
        this.firstCardId = Integer.parseInt(getProperty("card_0_id"));
    }
}
