package pages;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import utils.PropertyReader;

import java.io.File;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class ProfilePage {

    public ProfilePage openPage(String workspace) {
        open("https://" + workspace + ".kaiten.ru/profile");
        return this;
    }

    public ProfilePage isOpened() {
        $("[data-test-id='profile-card-header']").shouldBe(visible);
        return this;
    }

    public ProfilePage switchTheme(String theme) {
        $(byText("Внешний вид")).click();
        $(byTagAndText("p", "Внешний вид")).shouldBe(visible);
        $("[role='group']").find(byText(theme)).click();
        return this;
    }

    public void changeUserAvatar() {
        $(byText("Изменить")).click();
        $(byText("Загрузить аватар")).shouldBe(visible).click();
        $(byText("Перетащите изображение сюда")).shouldBe(visible);
        $("input[type='file']").uploadFile(new File("src/test/resources/images/photo.jpg"));
        $(byText("Обрезать и сохранить")).shouldBe(visible).click();
    }

    public boolean changeAvatarSuccessful() {
        $(byText("Аватар изменён")).shouldBe(visible);
        $(byText("Аватар изменён")).shouldNotBe(visible);
        return true;
    }

    public void changeUserName(String userName) {
        SelenideElement userNameInput = $x("//label[text()='Имя пользователя']/following::div/input");
        userNameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        userNameInput.shouldHave(attribute("value", ""));
        userNameInput.setValue(userName);
        userNameInput.shouldHave(attribute("value", userName));
        $("[data-test-id='general-information-save-btn']").shouldBe(enabled).click();
    }

    public boolean changeNameSuccessful(String userName) {
        SelenideElement userNameInput = $x("//label[text()='Имя пользователя']/following::div/input");
        refresh();
        return Objects.requireNonNull(userNameInput.getAttribute("value")).contains(userName);
    }

    public void changeAccountPassword(String oldPassword, String newPassword) {
        $(byText("Сменить пароль")).click();
        $(byText("Форма восстановления пароля")).shouldBe(visible);
        $(byName("oldPassword")).setValue(oldPassword);
        $(byName("newPassword")).setValue(newPassword);
        $(byName("newPasswordConfirmation")).setValue(newPassword);
        $("[data-testid='save-password-button']").shouldBe(enabled).click();
        PropertyReader.setProperty("password", newPassword);
        PropertyReader.saveProperties();
    }

    public boolean changeAccountPasswordSuccessful() {
        $(byText("Пароль успешно изменен")).shouldBe(visible);

        return true;
    }

    public boolean switchThemeSuccessful(String backgroundColor) {
        if (Objects.equals(backgroundColor, "rgba(255, 255, 255, 1)")) {
            $("#body").shouldHave(attribute("class", ""));
        } else {
            $("#body").shouldHave(attribute("class", "darkTheme"));
        }

        System.out.println($("body").getCssValue("background-color"));
        return $("body").getCssValue("background-color").equals(backgroundColor);
    }

    public void createApiToken() {
        $(byText("Ключ доступа API")).click();
        $(byText("Создать ключ")).shouldBe(visible).click();
        $(byText("Ключ успешно создан")).shouldBe(visible);

        String apiToken = $(".UserProfile-content").find("input").getAttribute("value");
        PropertyReader.setProperty("apiToken", apiToken);
        PropertyReader.saveProperties();

        $(byText("Ключ успешно создан")).shouldNotBe(visible);
    }

    public String getApiToken() {
        $(byText("Ключ доступа API")).click();
        $(".UserProfile-content input").shouldBe(visible);
        return $(".UserProfile-content input").getAttribute("value");
    }

    public boolean isApiTokenCreated() {
        return PropertyReader.getProperty("apiToken").isEmpty();
    }
}
