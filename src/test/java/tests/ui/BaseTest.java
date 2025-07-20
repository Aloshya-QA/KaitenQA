package tests.ui;

import api.tempMail.TempMailService;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RegistrationPage;
import pages.WorkspacePage;
import steps.LoginStep;
import utils.TestListener;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static initialization.BrowserOptions.getChromeOptions;
import static initialization.BrowserOptions.getEdgeOptions;
import static initialization.Init.startInit;
import static utils.PropertyReader.getProperty;

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
    String workspaceId;
    String firstCardName;
    String cardTag;
    String workspaceName;
    String firstCardId;

    TempMailService tempMail;
    SoftAssertions soft;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        initVariables();
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
        Configuration.browserSize = "1366x768";

        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
        profilePage = new ProfilePage();
        loginStep = new LoginStep();
    }

    @BeforeSuite(alwaysRun = true)
    private void init() throws InterruptedException {
        startInit();
    }

    private void initVariables() {
        this.email = getProperty("email");
        this.mailboxPassword = getProperty("mailboxPassword");
        this.workspace = getProperty("workspace");
        this.mailboxApiToken = getProperty("mailboxApiToken");
        this.kaitenApiToken = getProperty("kaitenApiToken");
        this.kaitenPassword = getProperty("kaitenPassword");
        this.workspaceId = getProperty("workspaceId");
        this.cardTag = getProperty("card_tag");
        this.firstCardName = getProperty("card_0_name");
        this.firstCardId = getProperty("card_0_id");
        this.workspaceName = getProperty("workspaceName");
        this.tempMail = new TempMailService();
        this.soft = new SoftAssertions();
    }

    @AfterMethod(alwaysRun = true)
    public void TearDawn() {
        if (getWebDriver() != null) {
            log.info("Closing browser");
            getWebDriver().quit();
        }
    }
}
