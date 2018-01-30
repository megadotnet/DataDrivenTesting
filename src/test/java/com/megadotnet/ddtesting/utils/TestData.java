package com.megadotnet.ddtesting.utils;

import com.megadotnet.ddtesting.base.Base;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

/*
 * Copyright (C) 2017 Megadotnet
 *
 *
 */

public class TestData {

    private static final Logger logger = Logger.getLogger(TestData.class);

    @DataProvider(name = "LoginData")
    public static Object[][] dataAdmin() throws Exception {
        logger.debug("Initializing the test data");
        return Base.getExcelToJson("Search");
    }
}