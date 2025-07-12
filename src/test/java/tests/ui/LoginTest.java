package tests.ui;

import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTest extends BaseTest {

    @Test(
            testName = "Successful login with pin",
            groups = {"Smoke", "Regression", "Pin"}
    )
    public void checkSuccessLoginWithPin() {
        loginPage
                .openPage(workspace)
                .isOpened()
                .inputEmail(email)
                .inputPin(tempMail.getPinCode());
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test(
            testName = "Successful login with password",
            groups = {"Regression", "Second"}
    )
    public void checkSuccessLoginWithPassword() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test(
            testName = "Checking login with wrong password",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongPassword() {
        loginStep.authWithPassword(email, workspace, data.getPassword());
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный логин или пароль");
    }

    @Test(
            testName = "Checking login with wrong pin",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongPin() {
        loginPage
                .openPage(workspace)
                .isOpened()
                .inputEmail(email)
                .inputPin(data.getPin());
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный PIN-код");
    }

    @Test(
            testName = "Checking login with wrong email",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongEmail() {
        loginStep.authWithPassword(data.getEmail(), workspace, kaitenPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный логин или пароль");
    }

    @Test(
            testName = "Checking logout",
            groups = {"Regression"}
    )
    public void checkLogout() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .logout();
        assertThat(loginPage.logoutSuccessful()).isTrue();
    }
}
