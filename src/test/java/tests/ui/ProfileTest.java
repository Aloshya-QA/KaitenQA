package tests.ui;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;
import utils.PropertyReader;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ProfileTest extends BaseTest{

    @Test(
            testName = "Создание API токена",
            groups = {"Initialization"}
    )
    public void getKaitenApiToken() {
        if (PropertyReader.getProperty("apiToken") != null) {
            log.warn("Skip getting apiToken. Api Token already exists");
            assertThat(PropertyReader.getProperty("apiToken")).isNotEmpty();
            return;
        }
        loginStep.authWithPin(email, workspace, tempMail.getPinCode());
        profilePage
                .openPage(workspace)
                .isOpened()
                .createApiToken();

        assertThat(profilePage.isApiTokenCreated()).isFalse();
        log.info("API Token is saved in a config.properties: " + PropertyReader.getProperty("apiToken"));
    }

    @Test
    public void checkSwitchTheme() {
        SoftAssertions softAssert = new SoftAssertions();
        loginStep.authWithPassword(email, workspace, password);
        profilePage
                .openPage(workspace)
                .isOpened()
                .switchTheme("Светлая");
        assertThat(profilePage.switchThemeSuccessful("rgba(255, 255, 255, 1)")).isTrue();
        profilePage.switchTheme("Тёмная");
        assertThat(profilePage.switchThemeSuccessful("rgba(33, 33, 33, 1)")).isTrue();
        softAssert.assertAll();

    }

    @Test
    public void checkChangeUserName() {
        loginStep.authWithPassword(email, workspace, password);
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeUserName("Nike111211");
        assertThat(profilePage.changeNameSuccessful("Nike111211")).isTrue();
    }

    @Test
    public void checkChangeAvatar() {
        loginStep.authWithPassword(email, workspace, password);
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeUserAvatar();
        assertThat(profilePage.changeAvatarSuccessful()).isTrue();
    }

    @Test
    public void checkChangeAccountPassword() {
        loginStep.authWithPassword(email, workspace, password);
        profilePage
                .openPage(workspace)
                .isOpened()
                .changeAccountPassword(password, "123Qwe12)))");
        assertThat(profilePage.changeAccountPasswordSuccessful()).isTrue();
    }
}
