import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;

public class TestDeliveryCard {

    private Faker faker;

    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    @Test
    void shouldRegisterByAccountNumber() {
        Configuration.headless = true;
        open("http://localhost:9999");
        String city = faker.address().city();
        String pastDate = faker.date().past(365, TimeUnit.DAYS).toString();
        String futureDate = faker.date().future(365, TimeUnit.DAYS).toString();
        String name = faker.name().lastName() + " " + faker.name().firstName();
        String phone = faker.phoneNumber().cellPhone();

        $(By.cssSelector("[data-test-id='city'] input")).setValue(city);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(pastDate);
        $(By.cssSelector("[data-test-id='name'] input")).setValue(name);
        $(By.cssSelector("[data-test-id='phone'] input")).setValue(phone);
        $(By.className("checkbox")).click();
        $(Selectors.byText("Запланировать")).click();
        $(By.cssSelector("[data-test-id='success-notification']")).shouldBe(Condition.visible, Duration.ofMillis(5000));

        open("http://localhost:9999");
        $(By.cssSelector("[data-test-id='city'] input")).setValue(city);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(futureDate);
        $(By.cssSelector("[data-test-id='name'] input")).setValue(name);
        $(By.cssSelector("[data-test-id='phone'] input")).setValue(phone);
        $(By.className("checkbox")).click();
        $(Selectors.byText("Запланировать")).click();
        $(By.cssSelector("[data-test-id='replan-notification']")).should(Condition.appear);
        $(Selectors.byText("Перепланировать")).click();
        $(By.cssSelector("[data-test-id='success-notification']")).should(Condition.appear);
    }

}


