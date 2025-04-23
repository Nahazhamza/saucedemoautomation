package tests;

import base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;

public class CheckoutTests extends TestBase {

    private LoginPage loginPage;
    private CartPage cartPage;
    private ProductsPage productsPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        productsPage = new ProductsPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(priority = 1, groups = {"sanity"})
    public void testCartItemCountAfterAdd() {
        System.out.println("Running testCartItemCountAfterAdd");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        productsPage.addItemToCart(0);
        productsPage.addItemToCart(1);
        productsPage.openCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");
    }

    @Test(priority = 2, groups = {"regression"})
    public void testFinishPurchaseDisplaysThankYouMessage() {
        System.out.println("Running testFinishPurchaseDisplaysThankYouMessage");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        productsPage.addItemToCart(0);
        productsPage.openCart();
        cartPage.proceedToCheckout();

        checkoutPage.enterShippingInfo("Jane", "Doe", "56789");
        checkoutPage.continueToOverview();
        checkoutPage.FinishButton();

        String confirmation = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmation.toLowerCase().contains("thank you"), "Expected confirmation message");
        
    }
}
