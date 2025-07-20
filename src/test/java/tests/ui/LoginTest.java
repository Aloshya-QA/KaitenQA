package tests.ui;

import factory.DataFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTest extends BaseTest {

    DataFactory data = new DataFactory();

    private final String
            wrongPassword = data.generatePassword(),
            wrongPin = data.generatePin(),
            wrongEmail = data.generateEmail();


    @Test(
            testName = "Check successful login with pin",
            groups = {"Smoke", "Regression", "Pin"}
    )
    public void checkSuccessLoginWithPin() {
        loginPage
                .openPage(workspace)
                .isOpened()
                .inputEmail(email)
                .inputPin(tempMail.getPin());
        workspacePage.isOpened();
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test(
            testName = "Check successful login with password",
            groups = {"Regression", "Second"}
    )
    public void checkSuccessLoginWithPassword() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test(
            testName = "Check login with wrong password",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongPassword() {
        loginStep.authWithPassword(email, workspace, wrongPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Login or password not valid");
    }

    @Test(
            testName = "Check login with wrong pin",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongPin() {
        loginPage
                .openPage(workspace)
                .isOpened()
                .inputEmail(email)
                .inputPin(wrongPin);
        assertThat(loginPage.getErrorMessage())
                .contains("PIN code is incorrect");
    }

    @Test(
            testName = "Check login with wrong email",
            groups = {"Regression"}
    )
    public void checkLoginWithWrongEmail() {
        loginStep.authWithPassword(wrongEmail, workspace, kaitenPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Login or password not valid");
    }

    @Test(
            testName = "Check logout",
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
