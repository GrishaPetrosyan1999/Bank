package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationUsers;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegistrationUsersTest {
    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        RegistrationUsers activeUser = DataGenerator.getActiveUser();
        $("[data-test-id=login] input").setValue(((RegistrationUsers) activeUser).getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();
        $(".App_appContainer__3jRx1").shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        RegistrationUsers blockedUser = DataGenerator.getBlockedUser();
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        RegistrationUsers invalidLoginUser = DataGenerator.getInvalidLoginUser("active");
        $("[data-test-id=login] input").setValue(invalidLoginUser.getLogin());
        $("[data-test-id=password] input").setValue(invalidLoginUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        RegistrationUsers invalidPassword = DataGenerator.getInvalidPasswordUser("active");
        $("[data-test-id=login] input").setValue(invalidPassword.getLogin());
        $("[data-test-id=password] input").setValue(invalidPassword.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
