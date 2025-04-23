package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CartPage {
    private WebDriver driver;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void proceedToCheckout() {
        checkoutButton.click();
    }

    public boolean isItemDisplayedInCart(String itemName) {
        for (WebElement item : cartItems) {
            if (item.getText().contains(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void removeItemFromCart(String itemName) {
        for (WebElement item : cartItems) {
            if (item.getText().contains(itemName)) {
                item.findElement(By.tagName("button")).click(); // Assumes the 'Remove' button is the only button inside the cart item
                break;
            }
        }
    }

    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
}
