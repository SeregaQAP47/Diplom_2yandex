package testPage.ByChrome;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.MainPage;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertEquals;

public class TestConstructor_Chrome {
    private WebDriver driver;
    private String URL = "https://stellarburgers.nomoreparties.site/";

    //Инициализация Chrome driver
    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver105.exe");
    }

    @Test
    @DisplayName("Переключение раздела Соусы")
    @Description("Тестирование переключения раздела Соусы" +
            "<p> Проверяем: </p>" +
            "<p> Раздел соусы переключается </p>")
    public void testSauce() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //"--start-maximized" - во весь экран
        driver = new ChromeDriver(options);

        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку Булки"), () -> {
            assertEquals("tab_tab__1SPyG  pt-4 pr-10 pb-4 pl-10 noselect", mainPage.getAttributeIngredients(1));
            mainPage.getAttributeIngredients(1);
            mainPage.clickButtonSauce();
        });
        step(("Проверяем что атрибут class изменился"), () -> {
            assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",
                    mainPage.getAttributeIngredients(1));
        });
    }

    @Test
    @DisplayName("Переключение раздела Булки")
    @Description("Тестирование переключения раздела Булки" +
            "<p> Проверяем: </p>" +
            "<p> Раздел соусы переключается </p>")
    public void testBun() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //"--start-maximized" - во весь экран
        driver = new ChromeDriver(options);

        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку Соусы"), () -> {
            mainPage.clickButtonSauce();
            assertEquals("tab_tab__1SPyG  pt-4 pr-10 pb-4 pl-10 noselect", mainPage.getAttributeIngredients(0));
        });
        step(("Нажать кнопку Булки"), () -> {
            mainPage.clickButtonBun();
        });
        step(("Проверяем что атрибут class изменился"), () -> {
            assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",
                    mainPage.getAttributeIngredients(0));
        });
    }

    @Test
    @DisplayName("Переключение раздела Начинки")
    @Description("Тестирование переключения раздела Начинки" +
            "<p> Проверяем: </p>" +
            "<p> Раздел соусы переключается </p>")
    public void testFilling() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox"); //"--start-maximized" - во весь экран
        driver = new ChromeDriver(options);

        MainPage mainPage = new MainPage(driver);
        step(("Открыть главную страницу"), () -> {
            driver.get(URL);
            mainPage.waitLoadPageMain();
        });
        step(("Нажать кнопку Булки"), () -> {
            assertEquals("tab_tab__1SPyG  pt-4 pr-10 pb-4 pl-10 noselect", mainPage.getAttributeIngredients(1));
            mainPage.getAttributeIngredients(2);
            mainPage.clickButtonFilling();
        });
        step(("Проверяем что атрибут class изменился"), () -> {
            assertEquals("tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect",
                    mainPage.getAttributeIngredients(2));
        });
    }

    @After
    public void teardown() {
        driver.quit();
    }

}
