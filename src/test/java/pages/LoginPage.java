package pages;

import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class LoginPage {

    public LoginPage openPage(String domain) {
        log.info("Opening loginPage...");
        open("https://" + domain + ".kaiten.ru/login");
        return this;
    }

    public LoginPage isOpened() {
        try {
            $("#email_username").shouldBe(visible);
            log.info("LoginPage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("LoginPage isn't opened");
        }

        return this;
    }

    public boolean logoutSuccessful() {
        log.info("Checking logout...");
        try {
            $("[data-action='kw-dialog-call']").shouldBe(visible);
            log.info("Logout successful");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Logout failed");
        }

        return true;
    }

    public LoginPage inputEmail(String email) {
        log.info("Inputting email: {}", email);
        $("#email_username").setValue(email).submit();
        $(byText("PIN-код отправлен на вашу почту")).shouldBe(visible);
        $(byText("PIN-код отправлен на вашу почту")).shouldNotBe(visible);
        return this;
    }

    public LoginPage inputPin(String pin) {
        log.info("Inputting pin: {}", pin);
        $("#pin").shouldBe(visible).setValue(pin).submit();
        return this;
    }

    public LoginPage loginWithPassword(String email, String password) {
        log.info("Inputting email: {}", email);
        $("#email_username").setValue(email);
        $("[data-test='use-password-btn']").click();
        log.info("Inputting password: {}", password);
        $("#password").shouldBe(visible).setValue(password).submit();
        return this;
    }

    public String getErrorMessage() {
        log.info("Getting error message...");
        return $("#notistack-snackbar").shouldBe(visible).getText();
    }
}
