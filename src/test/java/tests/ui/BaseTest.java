package tests.ui;

import api.tempMail.TempMailService;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import factory.DataFactory;
import factory.DataGenerator;
import initialization.InitSteps;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RegistrationPage;
import pages.WorkspacePage;
import steps.LoginStep;
import utils.TestListener;

import java.util.Collections;
import java.util.HashMap;

import static api.tempMail.TempMailService.getFirstDomain;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static utils.PropertyReader.*;

@Log4j2
@Listeners(TestListener.class)
public abstract class BaseTest {

    RegistrationPage registrationPage;
    LoginPage loginPage;
    WorkspacePage workspacePage;
    ProfilePage profilePage;
    LoginStep loginStep;

    String email;
    String mailboxPassword;
    String workspace;
    String mailboxApiToken;
    String kaitenApiToken;
    String kaitenPassword;

    TempMailService tempMail;
    DataFactory data;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        this.email = getProperty("email");
        this.mailboxPassword = getProperty("mailboxPassword");
        this.workspace = getProperty("workspace");
        this.mailboxApiToken = getProperty("mailboxApiToken");
        this.kaitenApiToken = getProperty("kaitenApiToken");
        this.kaitenPassword = getProperty("kaitenPassword");
        this.tempMail = new TempMailService();
        this.data = DataGenerator.dataFactory();

        RestAssured.filters(new AllureRestAssured());
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));

        if (browser.equalsIgnoreCase("chrome")) {
            log.info("Starting Chrome browser");
            Configuration.browser = "chrome";
            Configuration.browserCapabilities = getChromeOptions();

        } else if (browser.equalsIgnoreCase("edge")) {
            log.info("Starting Edge browser");
            Configuration.browser = "edge";
            Configuration.browserCapabilities = getEdgeOptions();
        }

        Configuration.baseUrl = "https://passport.kaiten.ru";
        Configuration.timeout = 10000;
        Configuration.clickViaJs = true;
//        Configuration.browserSize = "1366x768";
        Configuration.browserSize = null;

        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
        profilePage = new ProfilePage();
        loginStep = new LoginStep();
    }

    @BeforeSuite(alwaysRun = true)
    public void globalInit() {
        if (getProperty("kaitenApiToken") == null) {
            log.info("Start global initialization...");
            log.warn("Generating Test Data...");
            generateData();
            setup("chrome");
            log.info("Start creating mailbox...");
            InitSteps.createMailBox(tempMail);
            log.info("Start receiving mailbox API token...");
            InitSteps.createMailApiToken(tempMail);
            log.info("Start registering & activating Kaiten account...");
            InitSteps.registerAndActivate(email, workspace, tempMail);
            log.info("Start creating Kaiten API token...");
            InitSteps.createKaitenApiToken(workspace);
            log.info("Start creating Kaiten account password");
            InitSteps.setAccountPassword(getProperty("kaitenApiToken"), workspace, kaitenPassword);
            if (getWebDriver() != null) {
                log.info("Global initialization complete");
                getWebDriver().quit();
            }
        }
        log.info("Skip global initialization");
    }

    private void generateData() {
        String
                email = DataGenerator.generateEmailLogin(),
                mailboxPassword = DataGenerator.generatePassword(),
                kaitenPassword = DataGenerator.generatePassword(),
                workspace = DataGenerator.generateWorkspaceName();

        setProperty("email", email + getFirstDomain());
        setProperty("mailboxPassword", mailboxPassword);
        setProperty("kaitenPassword", kaitenPassword);
        setProperty("workspace", workspace);
        saveProperties();

        log.warn("Generating Test Data complete");
    }

    private static ChromeOptions getChromeOptions() {
        log.info("Chrome flags initialization");
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--lang=en");
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }

    private static EdgeOptions getEdgeOptions() {
        log.info("Edge flags initialization");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--lang=en");
        options.addArguments("--start-maximized");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }

    @AfterMethod(alwaysRun = true)
    public void TearDawn() throws InterruptedException {
        if (getWebDriver() != null) {
//            PropertyReader.clearProperties();
            Thread.sleep(10000);
            log.info("Closing browser");
            getWebDriver().quit();
        }
    }
}
