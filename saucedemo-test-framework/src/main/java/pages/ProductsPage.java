package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ProductsPage {
    private WebDriver driver;

    @FindBy(className = "title")
    private WebElement title;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(className = "btn_inventory")
    private List<WebElement> addToCartButtons;
    
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return title.getText();
    }

    public void logout() {
        menuButton.click();
        logoutLink.click();
    }

    public void addItemToCart(int index) {
        addToCartButtons.get(index).click();
    }

    public int getCartBadgeCount() {
        return Integer.parseInt(cartBadge.getText());
    }

    public void openCart() {
        cartIcon.click();
    }
    
    public WebElement getAddToCartButton(int index) {
        return driver.findElements(By.className("btn_inventory")).get(index); // Replace with actual selector if needed
    }
  
}
