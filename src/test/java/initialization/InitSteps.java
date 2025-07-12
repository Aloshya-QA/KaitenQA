package initialization;

import api.kaiten.KaitenService;
import api.kaiten.dto.request.CreatePasswordRq;
import api.tempMail.TempMailService;
import lombok.extern.log4j.Log4j2;
import pages.ProfilePage;
import pages.RegistrationPage;
import pages.WorkspacePage;
import utils.PropertyReader;

import static utils.PropertyReader.*;

@Log4j2
public final class InitSteps {

    public static void createMailBox(TempMailService mail) {
        mail.createMail();
    }

    public static void createMailApiToken(TempMailService mail) {
        PropertyReader.setProperty("mailboxApiToken", mail.getToken());
        PropertyReader.saveProperties();

        log.info("Mailbox token: {}", getProperty("mailboxApiToken"));

        if (PropertyReader.getProperty("mailboxApiToken").isEmpty()) {
            throw new IllegalStateException("Mailbox API token was NOT saved");
        }
        log.info("Mailbox API token saved");
    }

    public static void registerAndActivate(String email, String workspace, TempMailService mail) {
        new RegistrationPage()
                .openPage()
                .isOpened()
                .registerAccount(email, workspace)
                .isRegistrationComplete();

        new RegistrationPage()
                .activateCompany(mail.getActivateCompanyUrl());

        if (!new RegistrationPage().activateCompanySuccessful()) {
            throw new IllegalStateException("Account activation failed");
        }
        log.info("Account registered & activated: {}", email);
    }

    public static void createKaitenApiToken(String workspace) {
        if (getProperty("kaitenApiToken") != null) {
            log.warn("Kaiten API token already exists");
            return;
        }

        new WorkspacePage()
                .isOpened();

        new ProfilePage()
                .openPage(workspace)
                .isOpened()
                .createKaitenApiToken();

        if (!new ProfilePage().isApiTokenCreated()) {
            throw new IllegalStateException("Kaiten API token was NOT created");
        }

        log.info("Kaiten API token created and saved in config.properties");
    }

    public static void setAccountPassword(String token, String workspace, String newPassword) {
        KaitenService kaiten = new KaitenService();
        String userId = kaiten.getCurrentUserId(token, workspace);

        CreatePasswordRq rq = CreatePasswordRq.builder()
                .password(newPassword)
                .oldPassword(null)
                .build();
        kaiten.setPassword(rq, userId, token, workspace);
        PropertyReader.setProperty("kaitenPassword", newPassword);
        PropertyReader.saveProperties();
        log.info("Password '{}' set for user id: {}", newPassword, userId);
    }
}
