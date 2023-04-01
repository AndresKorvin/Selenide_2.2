package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class PositiveTests {


    private String setDate(int plusDays) {
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
//        Configuration.headless = true;
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }


    @ParameterizedTest
    @CsvSource({
            "Москва, 3, Андрей, +79996665544",
            "Санкт-Петербург, 4, Турчинский Владимир, +79996665544",
            "Краснодар, 1000, Соколова-Сероглазова, +79996665544",
            "Киров, 10000, Ким-Ер Сэн, +79996665544",
            "Владивосток, 100, Жёлудев Максим, +79996665544"

    })
    void positiveTests(String administrativeCenter, int plusDays, String name, String phone) {

        $("[data-test-id='city'] .input__control").setValue(administrativeCenter);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(plusDays));
        $("[data-test-id='name'] .input__control").setValue(name);
        $("[data-test-id='phone'] .input__control").setValue(phone);
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + setDate(plusDays)));

    }

    @ParameterizedTest
    @CsvSource({
            "Донецк", "Майкоп", "Горно-Алтайск", "Уфа", "Улан-Удэ", "Махачкала", "Донецк", "Магас", "Нальчик", "Элиста", "Черкесск",
            "Петрозаводск", "Сыктывкар", "Симферополь", "Луганск", "Йошкар-Ола", "Саранск", "Якутск", "Владикавказ", "Казань", "Кызыл",
            "Ижевск", "Абакан", "Грозный", "Чебоксары", "Барнаул", "Чита", "Петропавловск-Камчатский", "Краснодар", "Красноярск", "Пермь",
            "Владивосток", "Ставрополь", "Хабаровск", "Благовещенск", "Архангельск", "Астрахань", "Белгород", "Брянск", "Владимир", "Волгоград",
            "Вологда", "Воронеж", "Мелитополь", "Иваново", "Иркутск", "Калининград", "Калуга", "Кемерово", "Киров", "Кострома",
            "Курган", "Курск", "Гатчина", "Санкт-Петербург", "Липецк", "Магадан", "Красногорск", "Мурманск", "Нижний Новгород", "Великий Новгород",
            "Новосибирск", "Омск", "Оренбург", "Орёл", "Пенза", "Псков", "Ростов-на-Дону", "Рязань", "Самара", "Саратов",
            "Южно-Сахалинск", "Екатеринбург", "Смоленск", "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Ульяновск", "Херсон",
            "Челябинск", "Ярославль", "Москва", "Санкт-Петербург", "Севастополь", "Биробиджан", "Нарьян-Мар", "Ханты-Мансийск", "Анадырь", "Салехард"
    })
    void allTownTesting(String administrativeСenter) {

        $("[data-test-id='city'] .input__control").setValue(administrativeСenter);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(setDate(3));
        $("[data-test-id='name'] .input__control").setValue("Имя");
        $("[data-test-id='phone'] .input__control").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button span.button__text").click();

        System.out.println(administrativeСenter);
        $("[data-test-id=city].input_invalid .input__sub").shouldNotBe(visible);
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + setDate(3)));

    }
}