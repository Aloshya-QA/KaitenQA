package tests.ui;

import api.tempMail.TempMailService;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import factory.DataFactory;
import factory.DataGenerator;
import initialization.InitSteps;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RegistrationPage;
import pages.WorkspacePage;
import steps.LoginStep;
import utils.PropertyReader;
import utils.TestListener;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;

import static api.tempMail.TempMailService.getFirstDomain;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
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

    TempMailService tempMail;
    DataFactory data;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        String threadKey = Thread.currentThread().getName();
        String now = Instant.now().toString();

        PropertyReader.setProperty("testStartTime_" + threadKey, now);
        PropertyReader.saveProperties();
        log.info("Saved testStartTime_{} = {}", threadKey, now);

        this.email = getProperty("email");
        this.mailboxPassword = getProperty("mailboxPassword");
        this.workspace = getProperty("workspace");
        this.mailboxApiToken = getProperty("mailboxApiToken");
        this.kaitenApiToken = getProperty("kaitenApiToken");
        this.kaitenPassword = getProperty("kaitenPassword");
        this.tempMail = new TempMailService();
        this.data = DataGenerator.dataFactory();

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
        Configuration.timeout = 15000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = "1366x768";

        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
        profilePage = new ProfilePage();
        loginStep = new LoginStep();
    }

    @BeforeSuite(alwaysRun = true)
    public void globalInit() throws InterruptedException {
        if (getProperty("kaitenApiToken") == null) {
            generateData();
            setup("chrome");
            InitSteps.createMailBox(tempMail);
            InitSteps.createMailApiToken(tempMail);
            InitSteps.registerAndActivate(email, workspace, tempMail);
            InitSteps.createKaitenApiToken(email, workspace, tempMail);
            InitSteps.setAccountPassword(getProperty("kaitenApiToken"), workspace, kaitenPassword);
            getWebDriver().quit();
        }
        log.info("Skip Generation Data");
    }

    private void generateData() {
        log.warn("Generating Test Data...");

        String email = DataGenerator.generateEmailLogin();
        String mailboxPassword = DataGenerator.generatePassword();
        String kaitenPassword = DataGenerator.generatePassword();
        String workspace = DataGenerator.generateWorkspaceName();

        PropertyReader.setProperty("email", email + getFirstDomain());
        PropertyReader.setProperty("mailboxPassword", mailboxPassword);
        PropertyReader.setProperty("kaitenPassword", kaitenPassword);
        PropertyReader.setProperty("workspace", workspace);
        PropertyReader.saveProperties();
    }

    private static ChromeOptions getChromeOptions() {
        log.info("Init Chrome options");
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }

    private static EdgeOptions getEdgeOptions() {
        log.info("Init Edge options");
        EdgeOptions options = new EdgeOptions();
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
    public void TearDawn() {
        if (getWebDriver() != null) {
//            PropertyReader.clearProperties();
//            Thread.sleep(10000);
            log.info("Closing browser");
            getWebDriver().quit();
        }
    }
}
