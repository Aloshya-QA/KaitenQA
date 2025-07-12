package steps;

import pages.LoginPage;
import pages.WorkspacePage;

public class LoginStep {

    LoginPage loginPage;
    WorkspacePage workspacePage;

    public LoginStep() {
        loginPage = new LoginPage();
        workspacePage = new WorkspacePage();
    }

    public void authWithPassword(String email, String workspace, String password){
        loginPage
                .openPage(workspace)
                .isOpened()
                .loginWithPassword(email, password);
    }
}
