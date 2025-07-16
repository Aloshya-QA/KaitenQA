package tests.api;

import api.kaiten.KaitenService;
import org.testng.annotations.BeforeClass;

import static utils.PropertyReader.getProperty;

public class BaseApiTest {

    String token;
    String workspace;
    KaitenService kaiten;

    @BeforeClass
    public void loadProperties() {
        this.token = getProperty("kaitenApiToken");
        this.workspace = getProperty("workspace");
        this.kaiten = new KaitenService();
    }
}
