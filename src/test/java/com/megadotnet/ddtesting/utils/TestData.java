package com.megadotnet.ddtesting.utils;

import com.megadotnet.ddtesting.base.Base;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;


/*
 * Copyright (C) 2017 Megadotnet
 *
 *
 */
public class TestData {

    private static final Logger logger = LoggerFactory.getLogger(TestData.class);

    @DataProvider(name = "LoginData")
    public static Object[][] dataAdmin() throws Exception {
        logger.debug("Initializing the test data");
        return Base.getExcelToJson("Search");
    }
}