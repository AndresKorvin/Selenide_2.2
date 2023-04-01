package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SecondTask {

    public String setDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        Configuration.headless = true;
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void mustSelectACityByTwoLetters() {

        $("[data-test-id='city'] .input__control").setValue("мо");
        $x("//span[contains(text(),'Москва')]").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(3));
        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + setDate(3)));

    }

}
