package tests.ui;

import api.tempMail.TempMailService;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import factory.DataGenerator;
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

import java.util.Collections;
import java.util.HashMap;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@Log4j2
@Listeners(TestListener.class)
public class BaseTest {

    RegistrationPage registrationPage;
    LoginPage loginPage;
    WorkspacePage workspacePage;
    ProfilePage profilePage;
    LoginStep loginStep;

    String email;
    String password;
    String workspace;

    TempMailService tempMail;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        SelenideLogger.addListener("AllureSelenide",new AllureSelenide());

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
        Configuration.browserSize = null;

        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
        profilePage = new ProfilePage();
        loginStep = new LoginStep();
    }

    @BeforeSuite(alwaysRun = true)
    public void generateDataOnce() {
        generateData();
        this.email = PropertyReader.getProperty("email");
        this.password = PropertyReader.getProperty("password");
        this.workspace = PropertyReader.getProperty("workspace");
        this.tempMail = new TempMailService(email, password);
    }

    private void generateData() {
        log.warn("Generate Test Data");
        if (PropertyReader.getProperty("email") != null) {
            log.info("Skip generation");
            return;
        }

        String email = DataGenerator.generateEmailLogin();
        String password = DataGenerator.generatePassword();
        String workspace = DataGenerator.generateWorkspaceName();

        PropertyReader.setProperty("email", email + "@punkproof.com");
        PropertyReader.setProperty("password", password);
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
        log.info("Init Edge options");
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--disable-gpu");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        return options;
    }

    @AfterMethod(alwaysRun = true)
    public void TearDawn() {
        if (getWebDriver() != null) {
//            PropertyReader.clearProperties();
            log.info("Closing browser");
            getWebDriver().quit();
        }
    }
}
