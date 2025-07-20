package tests.ui;

import factory.DataFactory;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ProfileTest extends BaseTest{

    File pic = new File("src/test/resources/images/1.jpg");
    DataFactory data = new DataFactory();

    private final String
            username = data.generateUsername(),
            newPassword = data.generatePassword();
    
    @Test(
            testName = "Check switching theme",
            groups = {"Regression"}
    )
    public void checkSwitchTheme() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .switchTheme("Светлая");
        soft.assertThat(profilePage.switchThemeSuccessful("Светлая")).isTrue();
        profilePage.switchTheme("Тёмная");
        soft.assertThat(profilePage.switchThemeSuccessful("Тёмная")).isTrue();
        soft.assertAll();
    }

    @Test(
            testName = "Check changing username",
            groups = {"Regression"}
    )
    public void checkChangeUserName() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeUserName(username);
        assertThat(profilePage.changeNameSuccessful(username)).isTrue();
    }

    @Test(
            testName = "Check changing avatar",
            groups = {"Regression"}
    )
    public void checkChangeAvatar() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeUserAvatar(pic);
        assertThat(profilePage.changeAvatarSuccessful()).isTrue();
    }

    @Test(
            testName = "Check changing password",
            groups = {"Regression"}
    )
    public void checkChangeAccountPassword() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeAccountPassword(kaitenPassword, newPassword);
        assertThat(profilePage.changeAccountPasswordSuccessful()).isTrue();
    }
}
