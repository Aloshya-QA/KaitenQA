package pages;

import api.tempMail.TempMailService;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import java.time.Instant;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static utils.PropertyReader.saveProperties;
import static utils.PropertyReader.setProperty;

@Log4j2
public class LoginPage {

    private static final String
            EMAIL_INPUT = "#email_username",
            LOGOUT = "[data-action='kw-dialog-call']",
            NOTIFICATION = "#notistack-snackbar",
            PIN_INPUT = "#pin",
            USE_PASSWORD = "[data-test='use-password-btn']",
            PASSWORD_INPUT = "#password";

    @Step("Opening loginPage...")
    public LoginPage openPage(String domain) {
        log.info("Opening loginPage...");
        open("https://" + domain + ".kaiten.ru/login");
        return this;
    }

    @Step("LoginPage is opened")
    public LoginPage isOpened() {
        try {
            $(EMAIL_INPUT).shouldBe(visible);
            log.info("LoginPage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("LoginPage isn't opened");
        }

        return this;
    }

    @Step("Checking logout status...")
    public boolean logoutSuccessful() {
        log.info("Checking logout status...");
        return $(LOGOUT).shouldBe(visible).is(visible);
    }

    @Step("Inputting email: '{email}'")
    public LoginPage inputEmail(String email) {
        log.info("Inputting email: {}", email);
        $(EMAIL_INPUT).setValue(email).submit();
        $(NOTIFICATION).shouldBe(visible);
        $(NOTIFICATION).shouldNotBe(visible);
        return this;
    }

    @Step("Inputting pin: '{pin}'")
    public LoginPage inputPin(String pin) {
        log.info("Inputting pin: {}", pin);
        $(PIN_INPUT).shouldBe(visible).setValue(pin).submit();
        return this;
    }

    @Step("Login with email and password: '{email}', '{password}'")
    public LoginPage loginWithPassword(String email, String password) {
        log.info("Inputting email: {}", email);
        $(EMAIL_INPUT).setValue(email);
        $(USE_PASSWORD).click();
        log.info("Inputting password: {}", password);
        $(PASSWORD_INPUT).shouldBe(visible).setValue(password).submit();
        return this;
    }

    @Step("Getting error message...")
    public String getErrorMessage() {
        log.info("Getting error message...");
        if ($(NOTIFICATION).shouldBe(visible).getText().contains("PIN code is incorrect")) {
            saveUnusedPin();
            return "PIN code is incorrect";
        }
        return $(NOTIFICATION).shouldBe(visible).getText();
    }

    private void saveUnusedPin() {
        log.info("Starting save unused pin...");
        TempMailService mailbox = new TempMailService();
        String pin = mailbox.getPin();
        String receivedAt = Instant.now().toString();
        setProperty("unusedPin", pin);
        setProperty("unusedPinReceivedAt", receivedAt);
        saveProperties();
        log.info("Saved unused pin: {}, received at: {}", pin, receivedAt);
    }
}
