package homeWork2;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class MyHwSberTest extends AbstractTest {


    /**
     * 1. Перейти на страницу http://www.sberbank.ru/ru/person
     */
    @Before
    public void init() {
        driver.get(URL);
    }

    @Test
    public void testSber() {
        /**
         * 2.Нажать на – Страхование
         * 3.Выбрать – Путешествие и покупки
         */
        findElementXpath("//*[@aria-label='Меню Страхование']/span").click();
        findElementXpath("//*[@id='submenu-5']//*[contains(text(), 'Путешествия и покупки')]").click();

        /**
         * 4. Проверить наличие на странице заголовка – Страхование путешественников
         */
        Assert.assertEquals(findElementXpath("//h3[contains(text(),'Страхование путешественников')]").getText()
                , "Страхование путешественников");


        /**
         * 5. Нажать на – Оформить Онлайн
         * за 9 часов танцев с бубном, вокруг календаря, всегда срабатывала кнопка "Оформить Онлайн"
         */
        switchWindowByXpath("//*[@data-pid='SBRF-TEXT-2247407']//a[contains(text(), 'Оформить онлайн')]");

        /**
         * 6. На вкладке – Выбор полиса  выбрать сумму страховой защиты – Минимальная
         * 7. Нажать Оформить
         */
        findElementXpath("//*[contains(text(),'Минимальная')]").click();
        findElementXpath("//*[@ng-click='save()']").click();

        /**
         * 8. На вкладке Оформить заполнить поля:
         *    Фамилию и Имя, Дату рождения застрахованных
         *    Данные страхователя: Фамилия, Имя, Отчество, Дата рождения, Пол
         *    Паспортные данные
         *    Контактные данные не заполняем
         * 9. Проверить, что все поля заполнены правильно
         */

        fillInputByName("Фамилия /Surname", "Ivanov", "[@name='insured0_surname']");
        fillInputByName("Имя", "Ivan", "[@name='insured0_name']");

        fillInputByName("Фамилия", "Иванов", "[@name='surname']");
        fillInputByName("Имя", "Степан", "[@name='name']");
        fillInputByName("Отчество", "Иванович", "");
        findElementXpath("//h4[contains(text(),'Пол')]/following::span[1]").click();

        fillInputByName("Серия и номер паспорта", "5005", "[@name='passport_series']");
        findElementXpath("//*[@name='passport_number']").sendKeys("830330");
        checkErrorWithAttribute("//*[@name='passport_number']", "830330");
        findElementXpath("//*[@name='issuePlace']").sendKeys("ОУФМС МСК");
        checkErrorWithAttribute("//*[@name='issuePlace']", "ОУФМС МСК");

        setDate("//*[@id='views']/section/form/section/section[1]/div/insured-input/div/fieldset[4]/div/img", "6", "3", "1978");
        setDate("//*[@id='views']/section/form/section/section[2]/div/fieldset[7]/div/img", "15", "4", "1993");
        setDate("//*[@id='views']/section/form/section/section[3]/div/fieldset[2]/div/img", "21", "6", "2010");

        driver.switchTo().activeElement();

        checkErrorWithAttribute("//*[@name='insured0_birthDate']", "06.03.1978");
        checkErrorWithAttribute("//*[@name='birthDate']", "15.04.1993");
        checkErrorWithAttribute("//*[@name='issueDate']", "21.06.2010");

        /**
         * 10. Нажать продолжить
         * 11. Проверить, что появилось сообщение - Заполнены не все обязательные поля
         */
        findElementXpath("//*[@ng-click='save()']").click();

        Assert.assertTrue(findElementXpath("//div[contains(text(), 'Заполнены')]")
                .getText().contains("Заполнены не все обязательные поля"));
    }

}
