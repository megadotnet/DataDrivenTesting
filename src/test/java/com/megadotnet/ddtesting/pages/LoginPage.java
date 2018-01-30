package com.megadotnet.ddtesting.pages;

import com.megadotnet.ddtesting.base.Base;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 /*
 * Copyright (C) 2017 Megadotnet
 *
 *
 */

public class LoginPage extends Base {

   @FindBy(id = "txtUsername")
    WebElement txtUsername;

   @FindBy(id = "txtPassword")
   WebElement txtPassword;


   @FindBy(id="btnLogin")
   WebElement btnLogin;


   @FindBy(id="spanMessage")
   WebElement spnMessage;


    public void userLogin(JSONObject data) throws Exception {

        txtUsername.clear();
        txtUsername.sendKeys(data.getString("USERNAME"));
        txtPassword.clear();
        txtPassword.sendKeys(data.getString("PASSWORD"));
        btnLogin.click();

        System.out.println("Username " + data.getString("USERNAME") + " Password " + data.getString("PASSWORD"));
    }



    public String getMessage(){
        return spnMessage.getText();
    }
}

