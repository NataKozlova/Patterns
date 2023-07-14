import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;

public class TestDeliveryCard {

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldRegisterByAccountNumber() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $(By.cssSelector("[data-test-id='city'] input")).setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(firstMeetingDate);
        $(By.cssSelector("[data-test-id='name'] input")).setValue(validUser.getName());
        $(By.cssSelector("[data-test-id='phone'] input")).setValue(validUser.getPhone());
        $(By.className("checkbox")).click();
        $(Selectors.byText("Запланировать")).click();
        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content"))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(secondMeetingDate);
        $(Selectors.byText("Запланировать")).click();
        $(By.cssSelector("[data-test-id='replan-notification'] .notification__content"))
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .should(Condition.visible);
        $(Selectors.byText("Перепланировать")).click();
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content"))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .should(Condition.visible);
    }

}


