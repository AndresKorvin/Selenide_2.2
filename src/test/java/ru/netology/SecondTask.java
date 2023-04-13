package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.PositiveTests.setDate;

public class SecondTask {

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @ParameterizedTest
    @CsvSource({
            "мо, Москва", // первые буквы
            "рг, Санкт-Петербург",// последние буквы
            "ир, Киров",// буквы в середине
            "но, Краснодар",// буквы в середине
            "-н, Ростов-на-Дону",// дефис

    })
    void mustSelectCityByTwoLetters(String letters, String city) {

        $("[data-test-id='city'] .input__control").setValue(letters);
        String path = "//*[@class='menu-item__control'][contains(text(),'переменная')]";
        String xpath = path.replace("переменная", city);
        $x(xpath).click();

        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(3));
        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + setDate(3)));

    }

    @ParameterizedTest
    @CsvSource({
//            "1, 1, 2024",
//            "31, 1, 2024",
//            "1, 2, 2026",
//            "28, 2, 2026",
            "1, 3, 2024",
            "31, 3, 2024",
            "1, 4, 2024",
            "30, 4, 2024",
            "1, 5, 2024",
            "31, 5, 2024",
            "1, 6, 2024",
            "30, 6, 2024",
            "1, 7, 2023",
            "31, 7, 2023",
            "1, 8, 2023",
            "31, 8, 2023",
            "1, 9, 2023",
            "30, 9, 2023",
            "1, 10, 2023",
            "31, 10, 2023",
            "1, 11, 2023",
            "30, 11, 2023",
            "1, 12, 2023",
            "31, 12, 2023",

    })
    void mustSelectDateByCalendar(int day, int month, int year) {
        LocalDate targetDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        int countMonth = targetDate.getMonthValue() - now.getMonthValue();
        int countYear = targetDate.getYear() - now.getYear();

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $(".icon_name_calendar ").click();

        for (int i = 0; i < countYear; i++) {
            $(".calendar__arrow_direction_right[data-step='12']").click();
        }

        if (countMonth > 0) {
            for (int i = 0; i < Math.abs(countMonth); i++) {
                $(".calendar__arrow_direction_right[data-step='1']").click();
            }
        } else if (countMonth < 0) {
            for (int i = 0; i < Math.abs(countMonth); i++) {
                $(".calendar__arrow_direction_left[data-step='-1']").click();
            }
        }

        $$("td").filterBy(Condition.exactText(String.valueOf(day))).first().click();

        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").
                shouldHave(exactText("Встреча успешно забронирована на " + targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

    }

    @Test
    void dateSelectionForAGivenNumberOfDays() {
        LocalDate targetDate = LocalDate.now().plusDays(7);
        LocalDate now = LocalDate.now();
        int countMonth = targetDate.getMonthValue() - now.getMonthValue();
        int countYear = targetDate.getYear() - now.getYear();

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $(".icon_name_calendar ").click();

        for (int i = 0; i < countYear; i++) {
            $(".calendar__arrow_direction_right[data-step='12']").click();
        }

        if (countMonth > 0) {
            for (int i = 0; i < Math.abs(countMonth); i++) {
                $(".calendar__arrow_direction_right[data-step='1']").click();
            }
        } else if (countMonth < 0) {
            for (int i = 0; i < Math.abs(countMonth); i++) {
                $(".calendar__arrow_direction_left[data-step='-1']").click();
            }
        }

        $$("td").filterBy(Condition.exactText(String.valueOf(targetDate.getDayOfMonth()))).first().click();

        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").
                shouldHave(exactText("Встреча успешно забронирована на " + targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

    }
}
