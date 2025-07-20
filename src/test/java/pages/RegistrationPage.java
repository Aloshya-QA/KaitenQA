package pages;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class RegistrationPage {

    private static final String
            EMAIL_INPUT = "input[type='text']",
            DOMAIN_INPUT = "input[id='domain']",
            CHECKBOX_NEWS = "input[value='agree_news']",
            CHECKBOX_AGREE = "input[value='agree']",
            SUBMIT_BUTTON = "button[type='submit']",
            NEXT_BUTTON = "button[type='button']";


    @Step("Opening RegistrationPage...")
    public RegistrationPage openPage() {
        log.info("Opening RegistrationPage...");
        open("/ru/registration");
        return this;
    }

    @Step("RegistrationPage is opened")
    public RegistrationPage isOpened() {
        try {
            $(byText("Зарегистрироваться")).shouldBe(visible);
            log.info("RegistrationPage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("RegistrationPage isn't opened");
        }

        return this;
    }

    @Step("Registration account: email '{email}', company '{domain}'...")
    public RegistrationPage registerAccount(String email, String domain) {
        log.info("Registration account: email '{}', company '{}'", email, domain);
        log.info("Inputting email: {}", email);
        $(EMAIL_INPUT).setValue(email);
        log.info("Clearing domain field...");
        $(DOMAIN_INPUT).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $(DOMAIN_INPUT).shouldHave(attribute("value", ""));
        log.info("Domain field is clean");
        log.info("Inputting domain: {}", domain);
        $(DOMAIN_INPUT).setValue(domain);
        $(CHECKBOX_NEWS).click();
        $(CHECKBOX_AGREE).click();
        $(SUBMIT_BUTTON).click();

        registerAccountStepOne();
        registerAccountStepTwo();

        return this;
    }

    private void registerAccountStepOne() {
        log.info("Registration step one");
        $(byText("ГОТОВО, ЕДЕМ ДАЛЬШЕ")).shouldBe(visible);
        $(byText("IT")).click();
        $(byText("1-20")).click();
        $(byText("1-5")).click();
        $(NEXT_BUTTON).click();
    }

    private void registerAccountStepTwo() {
        log.info("Registration step two");
        $(byText("ПРОПУСТИТЬ")).shouldBe(visible);
        $(byText("ПРОПУСТИТЬ")).click();
    }

    @Step("Checking registration status...")
    public RegistrationPage isRegistrationComplete() {
        log.info("Checking registration status...");
        try {
            $(byText("Добро пожаловать")).shouldBe(visible);
            log.info("Registration complete");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Registration failed");
        }

        return this;
    }

    @Step("Activating company...")
    public RegistrationPage activateCompany(String url) {
        log.info("Activating company...");
        open(url);
        $(byText("Go to company")).shouldBe(visible).click();
        return this;
    }

    @Step("Activate company successful")
    public boolean activateCompanySuccessful() {
        try {
            $(byText("Открыт полный доступ на 14 дней \uD83E\uDD29")).shouldBe(visible);
            log.info("Activate company successful");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Activate company failed");
        }

        return true;
    }
}
