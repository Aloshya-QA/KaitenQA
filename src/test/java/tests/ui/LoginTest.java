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
            groups = {"Smoke", "Regression", "Pin"},
            priority = 1
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
            groups = {"Regression", "Second"},
            priority = 2
    )
    public void checkSuccessLoginWithPassword() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage.isOpened();
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test(
            testName = "Check login with wrong password",
            groups = {"Regression"},
            priority = 3
    )
    public void checkLoginWithWrongPassword() {
        loginStep.authWithPassword(email, workspace, wrongPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Login or password not valid");
    }

    @Test(
            testName = "Check login with wrong pin",
            groups = {"Regression"},
            priority = 4
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
            groups = {"Regression"},
            priority = 5
    )
    public void checkLoginWithWrongEmail() {
        loginStep.authWithPassword(wrongEmail, workspace, kaitenPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Login or password not valid");
    }

    @Test(
            testName = "Check logout",
            groups = {"Regression"},
            priority = 6
    )
    public void checkLogout() {
        loginStep.authWithPassword(email, workspace, kaitenPassword);
        workspacePage
                .isOpened()
                .logout();
        assertThat(loginPage.logoutSuccessful()).isTrue();
    }
}
