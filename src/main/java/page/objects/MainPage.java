package page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Главная страница
 */
public class MainPage {
    private WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    //Кнопка "Личный кабинет"
    private By buttonPersonalAccount = By.xpath(".//p[text()=\"Личный Кабинет\"]");
    //Кнопка "Войти в аккаунт"
    private By buttonEnterAccount = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_large__G21Vg");
    //Кнопка "Булки"
    private By buttonBun = By.xpath("//span[@class=\"text text_type_main-default\" and text()=\"Булки\"]");
    //Кнопка "Соусы"
    private By buttonSauce = By.xpath("//span[@class=\"text text_type_main-default\" and text()=\"Соусы\"]");
    //Кнопка "Начинки"
    private By buttonFilling = By.xpath("//span[@class=\"text text_type_main-default\" and text()=\"Начинки\"]");
    //Заголовок "Соберите бургер"
    private By headingAssembleTheBurger = By.xpath("//h1[text()=\"Соберите бургер\"]");
    //Кнопка "Оформить заказ"
    private By buttonPlaceAnOrder = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_large__G21Vg");

    private By classIngredients = By.cssSelector(".tab_tab__1SPyG.pt-4.pr-10.pb-4.pl-10.noselect");

    public void clickButtonPersonalAccount() {
        driver.findElement(buttonPersonalAccount).click();
    }

    public void clickButtonEnterAccount() {
        driver.findElement(buttonEnterAccount).click();
    }

    public void clickButtonBun() {
        driver.findElement(buttonBun).click();
    }

    public void clickButtonSauce() {
        driver.findElement(buttonSauce).click();
    }

    public void clickButtonFilling() {
        driver.findElement(buttonFilling).click();
    }

    public String getAttributeIngredients(int indexClass) {
        List<WebElement> element = driver.findElements(classIngredients);
        return element.get(indexClass).getAttribute("class");
    }

    public void waitLoadPageMain() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(headingAssembleTheBurger));
    }

    public String getTextPlaceAnOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(buttonPlaceAnOrder));
        return driver.findElement(buttonPlaceAnOrder).getText();
    }

    public String getTextHeading() {
        return driver.findElement(headingAssembleTheBurger).getText();
    }
}
