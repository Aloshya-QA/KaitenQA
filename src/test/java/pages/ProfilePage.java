package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import utils.PropertyReader;

import java.io.File;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.String.format;

@Log4j2
public class ProfilePage {

    private static final String
            PROFILE = "[data-test-id='profile-card-header']",
            THEMES_TAB = "Внешний вид",
            SWITCH_THEME_BUTTON = "//button[text()='%s']",
            USERNAME_INPUT = "//label[text()='Имя пользователя']/following::div/input",
            USERNAME_SAVE_BUTTON = "[data-test-id='general-information-save-btn']",
            PASSWORD_SAVE_BUTTON = "[data-testid='save-password-button']",
            NOTIFICATION = "#notistack-snackbar",
            OLD_PASSWORD_INPUT = "oldPassword",
            NEW_PASSWORD_INPUT = "newPassword",
            CONFIRM_PASSWORD_INPUT = "newPasswordConfirmation";


    @Step("Opening ProfilePage...")
    public ProfilePage openPage(String workspace) {
        log.info("Opening ProfilePage...");
        open("https://" + workspace + ".kaiten.ru/profile");
        return this;
    }

    @Step("ProfilePage is opened")
    public ProfilePage isOpened() {
        try {
            $(PROFILE).shouldBe(visible);
            log.info("ProfilePage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("ProfilePage isn't opened");
        }

        return this;
    }

    @Step("Switching the theme to a '{theme}'...")
    public ProfilePage switchTheme(String theme) {
        log.info("Opening themes tab...");
        $(byText(THEMES_TAB)).click();
        $(byTagAndText("p", THEMES_TAB)).shouldBe(visible);
        log.info("Themes tab is opened");
        log.info("Changing theme...");
        $x(format(SWITCH_THEME_BUTTON, theme)).click();
        $x(format(SWITCH_THEME_BUTTON, theme)).shouldHave(attribute("aria-pressed", "true"));
        return this;
    }

    @Step("Checking current theme...")
    public boolean switchThemeSuccessful(String theme) {
        log.info("Checking current theme...");
        return switch (theme) {
            case "Светлая" -> {

                log.info("Theme is light");
                yield $(
                        "body")
                        .getCssValue("background-color")
                        .equals("rgba(255, 255, 255, 1)"
                        );
            }
            case "Тёмная" -> {

                log.info("Theme is dark");
                yield $(
                        "body")
                        .getCssValue("background-color")
                        .equals("rgba(33, 33, 33, 1)"
                        );
            }
            default -> {

                log.info("Wrong value");
                yield false;
            }
        };
    }

    @Step("Changing user avatar...")
    public void changeUserAvatar(File file) {
        log.info("Changing avatar...");
        $(byText("Изменить")).click();
        log.info("Uploading avatar...");
        $(byText("Загрузить аватар")).shouldBe(visible).click();
        $(byText("Перетащите изображение сюда")).shouldBe(visible);
        $("input[type='file']").uploadFile(file);
        log.info("Saving avatar...");
        $(byText("Обрезать и сохранить")).shouldBe(visible).click();
    }

    @Step("Checking change user avatar status...")
    public boolean changeAvatarSuccessful() {
        try {
            $(byText("Аватар изменён")).shouldBe(visible);
            log.info("Avatar is changed");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("Avatar not changed");
        }

        return true;
    }

    @Step("Changing the username to '{username}'...")
    public void changeUserName(String username) {
        log.info("Changing the username...");
        SelenideElement userNameInput = $x(USERNAME_INPUT);
        log.info("Clearing input field...");
        userNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        userNameInput.shouldHave(attribute("value", ""));
        log.info("Input field is clean");
        log.info("Setting a username '{}'", username);
        userNameInput.setValue(username);
        userNameInput.shouldHave(attribute("value", username));
        log.info("Username is changed");
        $(USERNAME_SAVE_BUTTON).click();
        $(USERNAME_SAVE_BUTTON).shouldNotBe(clickable);
    }

    @Step("Checking change username status...")
    public boolean changeNameSuccessful(String userName) {
        log.info("Checking change username...");
        SelenideElement userNameInput = $x(USERNAME_INPUT);
        refresh();

        return Objects.requireNonNull(userNameInput.getAttribute("value")).contains(userName);
    }

    @Step("Changing the user old password '{oldPassword}' to new password '{newPassword}'...")
    public void changeAccountPassword(String oldPassword, String newPassword) {
        log.info("Changing password...");
        $(byText("Сменить пароль")).click();
        log.info("Opening change password tab...");
        $(byText("Форма восстановления пароля")).shouldBe(visible);
        log.info("Change password is opened");
        log.info("Inputting old password: {}", oldPassword);
        $(byName(OLD_PASSWORD_INPUT)).setValue(oldPassword);
        log.info("Inputting new password: {}", newPassword);
        $(byName(NEW_PASSWORD_INPUT)).setValue(newPassword);
        $(byName(CONFIRM_PASSWORD_INPUT)).setValue(newPassword);
        $(PASSWORD_SAVE_BUTTON).shouldBe(enabled).click();
        if ($(NOTIFICATION).getText().contains("Пароль успешно изменен")) {
            log.info("Password changed successful");
            log.info("Saving changed password...");
            PropertyReader.setProperty("kaitenPassword", newPassword);
            PropertyReader.saveProperties();
            log.info("Password is saved");
        }
    }

    @Step("Checking change password status...")
    public boolean changeAccountPasswordSuccessful() {
        return $(NOTIFICATION).getText().contains("Пароль успешно изменен");
    }

    @Step("Creating Kaiten API token...")
    public ProfilePage createKaitenApiToken() {
        log.info("Creating Kaiten API token...");
        $(byText("Ключ доступа API")).click();
        $(byText("Создать ключ")).shouldBe(visible).click();
        $(byText("Ключ успешно создан")).shouldBe(visible);
        log.info("Kaiten API token is created");

        String apiToken = $(".UserProfile-content").find("input").getAttribute("value");
        log.info("Saving Kaiten API token: " + apiToken);
        PropertyReader.setProperty("kaitenApiToken", apiToken);
        PropertyReader.saveProperties();
        log.info("Kaiten API token is saved");

        $(byText("Ключ успешно создан")).shouldNotBe(visible);
        return this;
    }

    @Step("Checking creating Kaiten API token status...")
    public boolean isApiTokenCreated() {
        log.info("Checking if Kaiten API token exists: {}", !PropertyReader.getProperty("kaitenApiToken").isEmpty());
        return !PropertyReader.getProperty("kaitenApiToken").isEmpty();
    }
}
