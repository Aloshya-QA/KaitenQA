package initialization;

import api.tempMail.TempMailService;
import com.codeborne.selenide.Configuration;
import factory.DataFactory;
import lombok.extern.log4j.Log4j2;

import static api.tempMail.TempMailService.getFirstDomain;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static initialization.BrowserOptions.getChromeOptions;
import static utils.PropertyReader.*;

@Log4j2
public class Init {

    private static String
            email,
            workspace,
            kaitenPassword;

    private static TempMailService
            tempMail;

    public static void startInit() throws InterruptedException {
        if (getProperty("kaitenApiToken") == null) {
            log.info("Start global initialization...");
            log.warn("Generating Test Data...");
            generateData();
            initVariables();
            setup();
            log.info("Start creating mailbox...");
            InitKaiten.createMailBox(tempMail);
            log.info("Start receiving mailbox API token...");
            InitKaiten.createMailApiToken(tempMail);
            log.info("Start registering & activating Kaiten account...");
            InitKaiten.registerAndActivate(email, workspace, tempMail);
            log.info("Start creating Kaiten API token...");
            InitKaiten.createKaitenApiToken(workspace);
            log.info("Start creating Kaiten account password");
            InitKaiten.setAccountPassword(getProperty("kaitenApiToken"), workspace, kaitenPassword);
            if (getWebDriver() != null) {
                log.info("Global initialization complete");
                getWebDriver().quit();
            }
        } else {
            log.info("Skip kaiten initialization");
        }


        if (getProperty("card_5_id") == null) {
            log.info("Start creating workspace...");
            InitWorkspace.creatingWorkspace();
            InitWorkspace.creatingBoard();
            InitWorkspace.creatingCards();
        } else {
            log.info("Skip workspace initialization");
        }
    }

    private static void generateData() {
        DataFactory data = new DataFactory();
        String
                email = data.generateEmail(),
                mailboxPassword = data.generatePassword(),
                kaitenPassword = data.generatePassword(),
                workspace = data.generateWorkspaceName();

        setProperty("email", email + getFirstDomain());
        setProperty("mailboxPassword", mailboxPassword);
        setProperty("kaitenPassword", kaitenPassword);
        setProperty("workspace", workspace);
        saveProperties();

        log.warn("Generating Test Data complete");
    }

    private static void initVariables() {
        email = getProperty("email");
        workspace = getProperty("workspace");
        kaitenPassword = getProperty("kaitenPassword");
        tempMail = new TempMailService();
    }

    private static void setup() {
        Configuration.browser = "chrome";
        Configuration.browserCapabilities = getChromeOptions();
        Configuration.baseUrl = "https://passport.kaiten.ru";
        Configuration.timeout = 10000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = "1366x768";
    }
}
