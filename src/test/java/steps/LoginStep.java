package steps;

import api.tempMail.TempMailService;
import pages.LoginPage;
import pages.WorkspacePage;

public class LoginStep {

    LoginPage loginPage;
    WorkspacePage workspacePage;

    public LoginStep() {
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
    }

    public void authWithPin(String email, String workspace, String pin){
            loginPage
                    .openPage(workspace)
                    .isOpened()
                    .inputEmail(email)
                    .inputPin(pin);
            workspacePage.isOpened();

    }

    public void authWithPassword(String email, String workspace, String password){
        loginPage
                .openPage(workspace)
                .isOpened()
                .loginWithPassword(email, password);
        workspacePage.isOpened();
    }
}
