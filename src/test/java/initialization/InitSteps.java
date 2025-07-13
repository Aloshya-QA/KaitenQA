package initialization;

import api.kaiten.KaitenService;
import api.kaiten.dto.request.CreatePasswordRq;
import api.tempMail.TempMailService;
import lombok.extern.log4j.Log4j2;
import pages.ProfilePage;
import pages.RegistrationPage;
import pages.WorkspacePage;

import static utils.PropertyReader.*;

@Log4j2
public final class InitSteps {

    public static void createMailBox(TempMailService mail) {
        mail.createMail();
        log.info("Mailbox created");
    }

    public static void createMailApiToken(TempMailService mail) {
        setProperty("mailboxApiToken", mail.getToken());
        saveProperties();

        if (getProperty("mailboxApiToken").isEmpty()) {
            throw new IllegalStateException("Mailbox API token was NOT saved");
        }

        log.info("Mailbox API token received & saved");
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
        new WorkspacePage()
                .isOpened();

        new ProfilePage()
                .openPage(workspace)
                .isOpened()
                .createKaitenApiToken();

        if (!new ProfilePage().isApiTokenCreated()) {
            throw new IllegalStateException("Kaiten API token was NOT created");
        }

        log.info("Kaiten API token created & saved");
    }

    public static void setAccountPassword(String token, String workspace, String newPassword) {
        String userId = KaitenService.getCurrentUserId(token, workspace);

        CreatePasswordRq rq = CreatePasswordRq.builder()
                .password(newPassword)
                .oldPassword(null)
                .build();
        KaitenService.setPassword(rq, userId, token, workspace);

        setProperty("kaitenPassword", newPassword);
        saveProperties();

        if (getProperty("kaitenPassword").isEmpty()) {
            throw new IllegalStateException("Password was NOT saved");
        }

        log.info("Password '{}' created", newPassword);
    }
}
