package homeWork2;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AbstractTest {
    protected static WebDriver driver;
    protected static String URL;
    public static Properties properties = TestProperties.getInstance().getProperties();

    @BeforeClass
    public static void setUp() {
        switch (properties.getProperty("browser")) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", properties.getProperty("webdriver.gecko.driver"));
                driver = new FirefoxDriver();
                break;
            default:
                System.setProperty("webdriver.chrome.driver", properties.getProperty("webdriver.chrome.driver"));
                driver = new ChromeDriver();
        }
        URL = properties.getProperty("app.url");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
        driver.quit();
    }

    /**
     * поиск элемента по Xpath, с ожиданием кликабельности елемента
     * @param stringXpath
     * @return
     */
    public WebElement findElementXpath(String stringXpath) {
        return new WebDriverWait(driver, 5000, 200)
                .until(ExpectedConditions.elementToBeClickable( By.xpath(stringXpath)));
    }

    /**
     * Метод для перевода driver в нужное окно после перехода по ссылке.
     * @param stringXpath
     */
    public void switchWindowByXpath(String stringXpath) {
        Set<String> oldWindowsSet = driver.getWindowHandles();
        /**
         * прикрутил JS click, но у меня и без него отрабатывает
         */
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", findElementXpath(stringXpath));

//      findElementXpath(stringXpath).click();

        String newWindowHandle = (new WebDriverWait(driver, 10))
                .until((ExpectedCondition<String>) driver -> {
                    Set<String> newWindowsSet = driver.getWindowHandles();
                    newWindowsSet.removeAll(oldWindowsSet);
                    return newWindowsSet.size() > 0 ?
                            newWindowsSet.iterator().next() : null;
                }
                );
        driver.switchTo().window(newWindowHandle);
    }

    /**
     * Заполнение полей по названию
     * @param name имя поля
     * @param textTo текст для заполнения
     * @param and добавление к имени поля, если требуется
     */
    public void fillInputByName(String name, String textTo, String and) {
        String temp = "//*[text() = '%s']/following::input[1]";
        String fullXpath = String.format((temp), name)+and;
        WebElement element = driver.findElement(By.xpath(fullXpath));
        element.sendKeys(textTo);
        new WebDriverWait(driver, 10).until(ExpectedConditions.textToBePresentInElementValue(element, textTo));
        /**
         * 8) Проверить, что все поля заполнены введенными значениями
         */
       checkErrorWithAttribute(fullXpath, textTo);
//
    }

    /**
     * Метод для проверки, соответсвия введых значений, полям
     * @param xpath
     * @param textTo
     */
    public void checkErrorWithAttribute(String xpath, String textTo) {
        WebElement element = findElementXpath(xpath);
        String actualText = element.getAttribute("value");
        Assert.assertEquals(textTo, actualText);
    }

    /**
     * Метод для установки даты...
     *
     *p/s может я конечно что то не так делал, но метод sendKeys() вставал комол,
     * любые поптыки ткнуть, по экрану, таб, enter и многое другое пробывал, перерыл пол интернета
     * @param day
     * @param month
     * @param year
     */
    public void setDate(String xpath, String day, String month, String year) {
        findElementXpath(xpath).click();
        Select selectYear = new Select(driver.findElement(By.xpath("//*[@class=\"ui-datepicker-year\"]")));
        selectYear.selectByValue(year);
        driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div/div/select[1]")).click();
        driver.findElement(By.xpath("//*[@class=\"ui-datepicker-month\"]/option[" + month + "]")).click();
        driver.findElement(By.xpath("//*[@data-event=\"click\"]//*[text()='" + day + "']")).click();
    }
}
