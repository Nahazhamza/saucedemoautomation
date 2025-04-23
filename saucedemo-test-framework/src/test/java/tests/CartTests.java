package tests;

import base.TestBase;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;

import java.time.Duration;

public class CartTests extends TestBase {

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
        System.out.println("Initialized LoginPage, ProductsPage, CartPage for CartTests");
    }

    @Test(priority = 1, groups = {"sanity", "smoke"})
    public void testAddItemToCart() {
        System.out.println("Test: Add item to cart - Starting");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(productsPage.getAddToCartButton(0)));

        productsPage.addItemToCart(0);
        productsPage.openCart();

        boolean isItemDisplayed = cartPage.isItemDisplayedInCart("Sauce Labs Backpack");
        Assert.assertTrue(isItemDisplayed, "Item was not displayed in the cart");

        System.out.println("Item successfully added to cart and verified.");
    }

    @Test(priority = 2, groups = {"sanity"})
    public void testCartIsEmptyAfterRemovingItem() {
        System.out.println("Test: Cart is empty after removing item - Starting");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(productsPage.getAddToCartButton(0)));

        productsPage.addItemToCart(0);
        productsPage.openCart();

        cartPage.removeItemFromCart("Sauce Labs Backpack");

        boolean isCartEmpty = cartPage.isCartEmpty();
        Assert.assertTrue(isCartEmpty, "Cart should be empty after removing the item");

        System.out.println("Cart is empty after item removal - Verified.");
    }
}
