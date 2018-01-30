package com.megadotnet.ddtesting.tests;

import com.megadotnet.ddtesting.base.Base;
import com.megadotnet.ddtesting.pages.LoginPage;
import com.megadotnet.ddtesting.utils.TestData;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

/*
 * Copyright (C) 2017 Megadotnet
 *
 *
 */

public class LoginPageTest extends Base {

    private static final Logger logger = Logger.getLogger(LoginPageTest.class);

    LoginPage loginPage;

    @BeforeClass
    public void beforeClass() throws Exception {
        driver.get(BASE_URL);
        loginPage = PageFactory.initElements(getWebDriver(), LoginPage.class);
        logger.info("Instance of login page is created");
    }

    @AfterClass
    public void afterClass() throws Exception {
        logger.info("Tests in LoginPageTest is completed");
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        logger.info("Refreshing the page with login page ");
        driver.get(BASE_URL);
        driver.navigate().refresh();
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        logger.info("AfterMethod");
    }

    @Test(dataProvider = "LoginData", dataProviderClass = TestData.class)
    public void testInvalidLogin(JSONObject data) throws Exception {
        loginPage.userLogin(data);
        Assert.assertEquals(loginPage.getMessage(), data.getString("ERROR_MSG"));
    }
}