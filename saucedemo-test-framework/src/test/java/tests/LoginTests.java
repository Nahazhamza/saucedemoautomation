package tests;

import base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.TestDataProvider;

import java.time.Duration;

public class LoginTests extends TestBase {

    private LoginPage loginPage;
    private CartPage cartPage;
    private ProductsPage productsPage;

    @BeforeMethod
    public void initPageObjects() {
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        productsPage = new ProductsPage(driver);

        System.out.println("Initialized page objects: LoginPage, CartPage, and ProductsPage.");
    }

    @Test(priority = 1, dataProvider = "loginData", dataProviderClass = TestDataProvider.class, groups = {"smoke"})
    public void testLoginScenarios(String username, String password, boolean isValid, String expectedError) {
        System.out.println("Test Login Scenarios - Starting for username: " + username);

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        
        System.out.println("Login attempted with username: " + username);

        if (isValid) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
            String title = customGetText(titleElement);
            Assert.assertEquals(title, "Products", "Title did not match expected");

            System.out.println("Login successful. Title matched: 'Products'");

            // Cookie check
            Cookie sessionCookie = driver.manage().getCookieNamed("session-username");
            if (sessionCookie != null) {
                String cookieValue = sessionCookie.getValue();
                System.out.println("session-username cookie value: " + cookieValue);
                Assert.assertEquals(cookieValue, username, "Cookie does not match logged-in username");
            } else {
                System.out.println("session-username cookie not found");
                Assert.fail("session-username cookie not found!");
            }

            productsPage.logout();
            System.out.println("User logged out successfully.");
        } else {
            System.out.println("Login failed, verifying error message...");
            Assert.assertEquals(loginPage.getErrorMessage(), expectedError, "Unexpected error message");
        }
    }

    @Test(priority = 2, groups = {"smoke", "sanity"})
    public void testCompletePurchaseFlow() {
        System.out.println("Test Complete Purchase Flow - Starting");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        
        System.out.println("Logged in with standard_user.");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(productsPage.getAddToCartButton(0)));

        System.out.println("Adding item to cart...");

        productsPage.addItemToCart(0);
        productsPage.openCart();
        
        System.out.println("Opening cart and proceeding to checkout...");

        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.enterShippingInfo("John", "Doe", "12345");
        checkoutPage.continueToOverview();
        checkoutPage.FinishButton();
        
        System.out.println("Checkout completed. Verifying confirmation message...");

      
        String confirmationMessage = checkoutPage.getConfirmationMessage().toLowerCase();
        Assert.assertTrue(confirmationMessage.contains("thank you for your order".toLowerCase()), "Confirmation message mismatch");

        System.out.println("Purchase flow completed successfully.");
    }

    @Test(priority = 3, groups = {"sanity"})
    public void testInvalidLoginWithEmptyCredentials() {
        System.out.println("Test Invalid Login with Empty Credentials - Starting");

        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        
        System.out.println("Login attempted with empty username and password.");

        // Validate error message
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface: Username is required"), "Error message did not match");

        System.out.println("Test for empty credentials passed. Error message displayed as expected.");
    }

    @Test(priority = 4, groups = {"sanity"})
    public void testInvalidLoginWithIncorrectPassword() {
        System.out.println("Test Invalid Login with Incorrect Password - Starting");

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();
        
        System.out.println("Login attempted with username 'standard_user' and incorrect password.");

        // Validate error message
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface: Username and password do not match"), "Error message did not match");

        System.out.println("Test for incorrect password passed. Error message displayed as expected.");
    }

    // Override of getText() for custom behavior
    public String customGetText(WebElement element) {
        try {
            String text = element.getText().trim();
            System.out.println("Custom getText(): " + text);
            return text;
        } catch (Exception e) {
            System.out.println("Error getting text: " + e.getMessage());
            return "";
        }
    }
}
