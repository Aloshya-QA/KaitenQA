package tests.ui;

import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginTest extends BaseTest {

    String wrongPassword = "123qwe12";
    String wrongPin = "1111";
    String wrongEmail = "asdasdad@mail.ru";

    @Test(
            testName = "Регистрация аккаунта",
            groups = {"Initialization"}
    )
    public void accountRegistration() throws InterruptedException {
        tempMail.createMail();
        registrationPage
                .openPage()
                .isOpened()
                .registerAccount(email, workspace)
                .isRegistrationComplete();
        assertThat(tempMail.getEmailSubjects().contains("Активация компании")).isTrue();
    }

    @Test(
            testName = "Активация аккаунта",
            groups = {"Initialization"}
    )
    public void activateAccount() {
        registrationPage
                .activateCompany(tempMail.getActivateCompanyUrl());
        assertThat(registrationPage.activateCompanySuccessful()).isTrue();
    }

    @Test
    public void checkSuccessLoginWithPin() {
        loginStep.authWithPassword(email, workspace, tempMail.getPinCode());
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test
    public void checkSuccessLoginWithPassword() {
        loginStep.authWithPassword(email, workspace, password);
        assertThat(workspacePage.isWorkspaceOpened()).isTrue();
    }

    @Test
    public void checkLoginWithWrongPassword() {
        loginStep.authWithPassword(email, workspace, wrongPassword);
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный логин или пароль");
    }

    @Test
    public void checkLoginWithWrongPin() {
        loginStep.authWithPin(email, workspace, wrongPin);
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный PIN-код");
    }

    @Test
    public void checkLoginWithWrongEmail() {
        loginStep.authWithPassword(wrongEmail, workspace, password);
        assertThat(loginPage.getErrorMessage())
                .contains("Неверный логин или пароль");
    }

    @Test
    public void checkLogout() {
        loginStep.authWithPassword(email, workspace, password);
        workspacePage
                .logout();
        assertThat(loginPage.logoutSuccessful()).isTrue();
    }
}
