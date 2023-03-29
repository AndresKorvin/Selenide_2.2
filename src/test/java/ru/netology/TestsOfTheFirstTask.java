package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import javax.net.ssl.KeyManagerFactorySpi;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class TestsOfTheFirstTask {
    @Test
    void tests() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");

        $("[data-test-id='city'] .input__control").setValue("Москва");

        String date = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),Keys.DELETE);
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] .input__control").setValue("Василий Теркин");

        $("[data-test-id='phone'] .input__control").setValue("+79996665544");

        $("[data-test-id='agreement']").click();

        $("button[role='button']").click();

        $(".notification__content").shouldBe(visible, Duration.ofSeconds(15));


    }
}