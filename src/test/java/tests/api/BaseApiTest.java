package tests.api;

import api.kaiten.KaitenService;
import org.testng.annotations.BeforeClass;
import utils.PropertyReader;

public class BaseApiTest {

    String token;
    String workspace;
    String password;
    KaitenService kaitenService;

    @BeforeClass
    public void loadProperties() {
        token = PropertyReader.getProperty("apiToken");
        workspace = PropertyReader.getProperty("workspace");
        password = PropertyReader.getProperty("password");
        kaitenService = new KaitenService();
    }
}
