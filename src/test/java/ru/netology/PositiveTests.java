package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class PositiveTests {


    private String setDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    @ParameterizedTest
    @CsvSource({
            "Москва, 3, Андрей, +79996665544",
            "Санкт-Петербург, 4, Турчинский Владимир, +79996665544",
            "Краснодар, 1000, Соколова-Сероглазова, +79996665544",
            "Киров, 10000, Ким-Ер Сэн, +79996665544",
            "Владивосток, 100, Жёлудев Максим, +79996665544"

    })
    void positiveTests(String administrativeСenter, int plusDays, String name, String phone) {

//        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999/");

        $("[data-test-id='city'] .input__control").setValue(administrativeСenter);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(plusDays));
        $("[data-test-id='name'] .input__control").setValue(name);
        $("[data-test-id='phone'] .input__control").setValue(phone);
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + setDate(plusDays)));

    }
}