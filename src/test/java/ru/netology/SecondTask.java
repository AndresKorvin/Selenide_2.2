package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SecondTask {

    public String setDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

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
            "1, 0, 2024",
            "31, 0, 2024",
            "1, 1, 2024",
            "28, 1, 2024",
            "1, 2, 2024",
            "31, 2, 2024",
            "1, 3, 2024",
            "30, 3, 2024",
            "1, 4, 2024",
            "31, 4, 2024",
            "1, 5, 2024",
            "30, 5, 2024",
            "1, 6, 2023",
            "31, 6, 2023",
            "1, 7, 2023",
            "31, 7, 2023",
            "1, 8, 2023",
            "30, 8, 2023",
            "1, 9, 2023",
            "31, 9, 2023",
            "1, 10, 2023",
            "30, 10, 2023",
            "1, 11, 2023",
            "31, 11, 2023",

    })
    void mustSelectDateByCalendar(int day, int month, int year) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        DateFormat monthFormat = new SimpleDateFormat("MMMM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat dmyFormat = new SimpleDateFormat("dd.MM.yyyy");

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $(".icon_name_calendar ").click();

        while (!($(".calendar__name").getText().toLowerCase().contains(monthFormat.format(calendar.getTime())))) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        while (!($(".calendar__name").getText().toLowerCase().contains(yearFormat.format(calendar.getTime())))) {
            $(".calendar__arrow_direction_right[data-step='12']").click();
        }
        $$("td").filterBy(Condition.exactText(String.valueOf(day))).first().click();

        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").
                shouldHave(exactText("Встреча успешно забронирована на " + dmyFormat.format(calendar.getTime())));

    }

    @Test
    void dateSelectionForAGivenNumberOfDays() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        DateFormat dayFormat = new SimpleDateFormat("d");
        DateFormat monthFormat = new SimpleDateFormat("MMMM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat dmyFormat = new SimpleDateFormat("dd.MM.yyyy");


        $("[data-test-id='city'] .input__control").setValue("Москва");
        $(".icon_name_calendar ").click();

        while (!($(".calendar__name").getText().toLowerCase().contains(monthFormat.format(calendar.getTime())))) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        while (!($(".calendar__name").getText().toLowerCase().contains(yearFormat.format(calendar.getTime())))) {
            $(".calendar__arrow_direction_right[data-step='12']").click();
        }

        $$("td").filterBy(Condition.exactText(dayFormat.format(calendar.getTime()))).first().click();

        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").
                shouldHave(exactText("Встреча успешно забронирована на " + dmyFormat.format(calendar.getTime())));

    }

}
