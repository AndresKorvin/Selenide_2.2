package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class NegativeTest {

    public String setDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    static ArrayList townList = townsList();

    static ArrayList townsList() {
        Configuration.headless = true;
        open("https://ru.wikipedia.org/wiki/%D0%90%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B8%D0%B2%D0%BD%D1%8B%D0%B5_%D1%86%D0%B5%D0%BD%D1%82%D1%80%D1%8B_%D1%81%D1%83%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D0%BE%D0%B2_%D0%A0%D0%BE%D1%81%D1%81%D0%B8%D0%B9%D1%81%D0%BA%D0%BE%D0%B9_%D0%A4%D0%B5%D0%B4%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D0%B8");
        ArrayList list = (ArrayList) $$("tr>td+td+td a[title]").texts();
        return list;
    }

    @BeforeEach
    void setUp() {
//        Configuration.headless = true ;
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @ParameterizedTest
    @CsvSource({
            "Moscow",
            "!!!",
            "Лондон",
            "Яранск",

    })
    void shouldNotTakeTown(String town) {

        $("[data-test-id='city'] .input__control").setValue(town);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();

        if (townList.contains(town)) {
            Assertions.fail("!Тестовые данные содержат город из списка Административных центров!");
        } else {
            $("[data-test-id=city].input_invalid .input__sub")
                    .shouldHave(exactText("Доставка в выбранный город недоступна"));
        }

    }

    @ParameterizedTest
    @CsvSource({
            "-500",
            "-1",
            "0",
            "1",
            "2",

    })
    void shouldNotTakeDate(int day) {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(day));
        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));

    }

    @ParameterizedTest
    @CsvSource({
            "Alex", // латиница
            "Турчинский~", // спецсимволы
            "Турчинский!", // спецсимволы
            "Турчинский№", // спецсимволы
            "Турчинский;", // спецсимволы
            "Турчинский%", // спецсимволы
            "Турчинский:", // спецсимволы
            "Турчинский?", // спецсимволы
            "Турчинский*", // спецсимволы
            "Турчинский+", // спецсимволы
            "Турчинский=", // спецсимволы
            "Турчинский/", // спецсимволы
            "Турчинский.", // спецсимволы
            "123," // цифры
    })
    void shouldNotTakeName(String name) {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='name'] .input__control").setValue(name);
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }

    @ParameterizedTest
    @CsvSource({
            "Турчинский, 1", // 1 цифра, без +
            "Турчинский, +1", // 1 цифра

            "Турчинский, 9996665544", // 10 цифр, без +
            "Турчинский, +9996665544", // 10 цифр

            "Турчинский, 799966655443", // 12 цифр, без +
            "Турчинский, +799966655443", // 12 цифр

            "Турчинский, +абвгдежзикл", // кириллица 11 букв с +
            "Турчинский, +qwertyuiopa", // латиница 11 букв с +

            "Турчинский, йцукен", // кириллица
            "Турчинский, qwerty", // латиница

            "Турчинский, +9996665544~", // спецсимволы
            "Турчинский, +7999666554`", // спецсимволы
            "Турчинский, +7999666554!", // спецсимволы
            "Турчинский, +7999666554@", // спецсимволы
            "Турчинский, +7 999 999 9999", // пробелы

    })
    void shouldNotTakePhone(String name, String phone) {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='name'] .input__control").setValue(name);
        $("[data-test-id='phone'] .input__control").setValue(phone);
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));

    }

    @Test
    void emptyFieldPhone() {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='name'] .input__control").setValue("Турчинский");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $("[data-test-id=phone].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    void emptyFieldName() {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $("[data-test-id=name].input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));

    }

    @Test
    void notCheckBox() {

        $("[data-test-id='city'] .input__control").setValue("Москва");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(4));
        $("[data-test-id='name'] .input__control").setValue("Турчинский");
        $("[data-test-id='phone'] .input__control").setValue("+79996665544");
        $("button span.button__text").click();
        $("[data-test-id=agreement].input_invalid .input__sub").isDisplayed();
    }
}
