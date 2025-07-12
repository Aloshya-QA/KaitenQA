package pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class RegistrationPage {

    public RegistrationPage openPage() {
        log.info("Opening RegistrationPage...");
        open("/ru/registration");
        return this;
    }

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

    public RegistrationPage registerAccount(String email, String domain) {
        log.info("Registration account: email '{}', company '{}'", email, domain);
        log.info("Inputting email: {}", email);
        $("input[type='text']").setValue(email);
        log.info("Clearing domain field...");
        $("input[id='domain']").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("input[id='domain']").shouldHave(attribute("value", ""));
        log.info("Domain field is clean");
        log.info("Inputting domain: {}", domain);
        $("input[id='domain']").setValue(domain);
        $("input[value='agree_news']").click();
        $("input[value='agree']").click();
        $("button[type='submit']").click();

        registerAccountStepOne();
        registerAccountStepTwo();
//        registerAccountStepThree();

        return this;
    }

    private void registerAccountStepOne() {
        log.info("Registration step one");
        $(byText("ГОТОВО, ЕДЕМ ДАЛЬШЕ")).shouldBe(visible);
        $(byText("IT")).click();
        $(byText("1-20")).click();
        $(byText("1-5")).click();
        $("button[type='button']").click();
    }

    private void registerAccountStepTwo() {
        log.info("Registration step two");
        $(byText("ПРОПУСТИТЬ")).shouldBe(visible);
        $(byText("ПРОПУСТИТЬ")).click();
    }

    private void registerAccountStepThree() {
        log.info("Registration step three");
        $("div[role='progressbar']").shouldBe(visible);
        $("div[role='progressbar']").shouldNotBe(visible);
        $("button[title='Clear']").hover().click();
        $("input[type='text']").setValue("Польша").sendKeys(Keys.ENTER);
        $(byText("Далее")).shouldBe(visible);
        $(byText("Далее")).click();
    }

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

    public RegistrationPage activateCompany(String url) {
        log.info("Activating company...");
        open(url);
        return this;
    }

    public boolean activateCompanySuccessful() {
        try {
            $(byText("Перейти в компанию")).shouldBe(visible);
            log.info("Activate company successful");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Activate company failed");
        }

        return true;
    }
}
