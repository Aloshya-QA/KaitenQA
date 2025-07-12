package pages;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import utils.PropertyReader;

import java.io.File;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class ProfilePage {

    public ProfilePage openPage(String workspace) {
        log.info("Opening ProfilePage...");
        open("https://" + workspace + ".kaiten.ru/profile");
        return this;
    }

    public ProfilePage isOpened() {
        try {
            $("[data-test-id='profile-card-header']").shouldBe(visible);
            log.info("ProfilePage is opened");
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail("ProfilePage isn't opened");
        }

        return this;
    }

    public ProfilePage switchTheme(String theme) {
        log.info("Opening themes tab...");
        $(byText("Внешний вид")).click();
        $(byTagAndText("p", "Внешний вид")).shouldBe(visible);
        log.info("Themes tab is opened");
        log.info("Changing theme...");
        $("[role='group']").find(byText(theme)).click();
        return this;
    }

    public boolean switchThemeSuccessful(String backgroundColor) {
        log.info("Checking current theme...");
        if (Objects.equals(backgroundColor, "rgba(255, 255, 255, 1)")) {
            log.info("Theme is light");
            $("#body").shouldHave(attribute("class", ""));
        } else {
            log.info("Theme is dark");
            $("#body").shouldHave(attribute("class", "darkTheme"));
        }

        return $("body").getCssValue("background-color").equals(backgroundColor);
    }

    public void changeUserAvatar() {
        log.info("Changing avatar...");
        $(byText("Изменить")).click();
        log.info("Uploading avatar...");
        $(byText("Загрузить аватар")).shouldBe(visible).click();
        $(byText("Перетащите изображение сюда")).shouldBe(visible);
        $("input[type='file']").uploadFile(new File("src/test/resources/images/photo.jpg"));
        log.info("Saving avatar...");
        $(byText("Обрезать и сохранить")).shouldBe(visible).click();
    }

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

    public void changeUserName(String userName) {
        log.info("Changing the username...");
        SelenideElement userNameInput = $x("//label[text()='Имя пользователя']/following::div/input");
        log.info("Clearing input field...");
        userNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        userNameInput.shouldHave(attribute("value", ""));
        log.info("Input field is clean");
        log.info("Setting a username '{}'", userName);
        userNameInput.setValue(userName);
        userNameInput.shouldHave(attribute("value", userName));
        log.info("Username is changed");
        $("[data-test-id='general-information-save-btn']").click();
        $("[data-test-id='general-information-save-btn']").shouldNotBe(clickable);
    }

    public boolean changeNameSuccessful(String userName) {
        log.info("Checking change username...");
        SelenideElement userNameInput = $x("//label[text()='Имя пользователя']/following::div/input");
        refresh();
        return Objects.requireNonNull(userNameInput.getAttribute("value")).contains(userName);
    }

    public void changeAccountPassword(String oldPassword, String newPassword) {
        log.info("Changing password...");
        $(byText("Сменить пароль")).click();
        log.info("Opening change password tab...");
        $(byText("Форма восстановления пароля")).shouldBe(visible);
        log.info("Change password is opened");
        log.info("Inputting old password: {}", oldPassword);
        $(byName("oldPassword")).setValue(oldPassword);
        log.info("Inputting new password: {}", newPassword);
        $(byName("newPassword")).setValue(newPassword);
        $(byName("newPasswordConfirmation")).setValue(newPassword);
        $("[data-testid='save-password-button']").shouldBe(enabled).click();
        if ($("#notistack-snackbar").getText().contains("Пароль успешно изменен")) {
            log.info("Password changed successful");
            log.info("Saving changed password...");
            PropertyReader.setProperty("kaitenPassword", newPassword);
            PropertyReader.saveProperties();
            log.info("Password is saved");
        }
    }

    public boolean changeAccountPasswordSuccessful() {
        return $("#notistack-snackbar").getText().contains("Пароль успешно изменен");
    }

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

    public boolean isApiTokenCreated() {
        log.info("Checking if Kaiten API token exists: {}", !PropertyReader.getProperty("kaitenApiToken").isEmpty());
        return !PropertyReader.getProperty("kaitenApiToken").isEmpty();
    }
}
