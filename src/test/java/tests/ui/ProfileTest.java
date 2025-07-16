package tests.ui;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ProfileTest extends BaseTest{

    File pic = new File("src/test/resources/images/photo.jpg");

    @Test(
            testName = "Checking switching theme",
            groups = {"Regression"}
    )
    public void checkSwitchTheme() {
        SoftAssertions softAssert = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .switchTheme("Светлая");
        assertThat(profilePage.switchThemeSuccessful("rgba(255, 255, 255, 1)")).isTrue();
        profilePage.switchTheme("Тёмная");
        assertThat(profilePage.switchThemeSuccessful("rgba(33, 33, 33, 1)")).isTrue();
        softAssert.assertAll();

    }

    @Test(
            testName = "Checking changing username",
            groups = {"Regression"}
    )
    public void checkChangeUserName() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeUserName(data.getUsername());
        assertThat(profilePage.changeNameSuccessful(data.getUsername())).isTrue();
    }

    @Test(
            testName = "Checking changing avatar",
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
            testName = "Checking changing password",
            groups = {"Regression"}
    )
    public void checkChangeAccountPassword() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeAccountPassword(kaitenPassword, data.getPassword());
        assertThat(profilePage.changeAccountPasswordSuccessful()).isTrue();
    }
}
