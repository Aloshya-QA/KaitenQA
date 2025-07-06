package pages;

import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class WorkspacePage {

    public WorkspacePage isOpened() {
        try {
            $("[data-testid='app-bar-profile-avatar']").shouldBe(visible);
            log.info("WorkspacePage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("WorkspacePage isn't opened");
        }
        return this;
    }

    public boolean isWorkspaceOpened() {
        log.warn("Check status WorkspacePage...");
        try {
            $("[data-testid='app-bar-profile-avatar']").shouldBe(visible);
            log.warn("Is WorkspacePage: True");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Is WorkspacePage: False");
        }

        return true;
    }

    public LoginPage logout() {
        $("[data-testid='app-bar-profile-avatar']").click();
        $("[data-testid='app-bar-profile-logout-button']").shouldBe(visible).click();
        return new LoginPage();
    }
}
