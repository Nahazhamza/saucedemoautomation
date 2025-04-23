package utils;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
    @DataProvider(name = "loginData")
    public Object[][] provideLoginData() {
        return new Object[][]{
            {"standard_user", "secret_sauce", true, ""},
            {"problem_user", "secret_sauce", true, ""},
            {"locked_out_user", "secret_sauce", false, "Epic sadface: Sorry, this user has been locked out."},
            {"", "", false, "Epic sadface: Username is required"}
            
        };
    }
}