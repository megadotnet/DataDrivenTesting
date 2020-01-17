package com.megadotnet.ddtesting.base;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

/*
 * Copyright (C) 2017 Megadotnet
 *
 *
 */

public class Base {

    private static final Logger logger = LoggerFactory.getLogger(Base.class);

    protected static String url;
    protected static WebDriver driver;
    protected static Properties properties;
    protected boolean acceptNextAlert = true;
    protected static StringBuffer verificationErrors = new StringBuffer();

    protected static String BROWSER_TYPE;
    protected static int TIME_OUT;
    protected static String FIELD_DELIMITER;
    protected static String DATA_DELIMITER;

    protected static String BASE_URL;
    protected static String USERNAME;
    protected static String PASSWORD;

    @BeforeSuite
    public void beforeSuite() throws Exception {
        properties = new Properties();
        try {

            properties.load(new FileInputStream("src/test/resources/test.properties"));

            BROWSER_TYPE = properties.getProperty("BROWSER_TYPE");
            TIME_OUT = Integer.parseInt(properties.getProperty("TIME_OUT"));
            FIELD_DELIMITER = properties.getProperty("FIELD_DELIMITER");
            DATA_DELIMITER = properties.getProperty("DATA_DELIMITER");

            BASE_URL = properties.getProperty("BASE_URL");
            USERNAME = properties.getProperty("USERNAME");
            PASSWORD = properties.getProperty("PASSWORD");

            driver = getWebDriver();

        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(0);
        }
    }

    @AfterSuite
    public void afterSuite() throws Exception {
        driver.quit();
    }

    protected WebDriver getWebDriver() throws Exception {

        if (BROWSER_TYPE.equalsIgnoreCase("HtmlUnitDriver")) {
            logger.info("Starting HtmlUnitDriver ");
            driver = new HtmlUnitDriver();
        } else if (BROWSER_TYPE.equalsIgnoreCase("Firefox")) {
			final File file = new File("E:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
            System.setProperty("webdriver.firefox.bin", file.getAbsolutePath());
            logger.info("Staring Firefox");		
            driver = new FirefoxDriver();
        } else if (BROWSER_TYPE.equalsIgnoreCase("Chrome")) {
            final File file = new File("driver/chromedriver.exe");
            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            driver = new ChromeDriver();
        } else if (BROWSER_TYPE.equalsIgnoreCase("InternetExplorer")) {
            final File file = new File("driver/IEDriverServer.exe");
            System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
            driver = new InternetExplorerDriver();
        } else if (BROWSER_TYPE.equalsIgnoreCase("HtmlUnit")) {
            driver = new HtmlUnitDriver();
        } else {
            logger.info("BROWSER_TYPE not identified. Starting default type");
            driver = new HtmlUnitDriver();
        }

        logger.info("BROWSER_TYPE=" + BROWSER_TYPE);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        return driver;
    }

    protected void clearAndType(By by, String text) throws Exception {
        WebElement field = driver.findElement(by);
        try {
            field.clear();
            field.sendKeys(text);
        } catch (InvalidElementStateException e) {
            logger.info(e.toString());
        }
    }

    protected void readAndCompare(By by, String text) throws Exception {
        WebElement field = driver.findElement(by);
        if (!field.getText().equals(text)) {
            verificationErrors.append("\nElement: ");
            verificationErrors.append(by.toString());
            verificationErrors.append("\nExpected Text: ");
            verificationErrors.append(text);
            verificationErrors.append("\nActual Text: ");
            verificationErrors.append(field.getAttribute("value"));
        }
    }

    protected void click(By by) throws Exception {
        driver.findElement(by).click();
    }

    protected void click(By by, String state) throws Exception {
        if (state.equals("CHECKED")) {
            logger.info(by.toString() + ". Clicking since the state is set to 'CHECKED'");
            click(by);
        } else {
            logger.info(by.toString() + ". No action taken since the state is NOT set to 'CHECKED'");
        }
    }

    protected void selectAutoComplete(By byField, By byPopUp, String visibleText) throws Exception {

        driver.findElement(byField).sendKeys(visibleText);
        sleep(TIME_OUT);
        List<WebElement> webElementList = driver.findElements(byPopUp);
        for (WebElement e : webElementList) {
            if (e.getText().startsWith(visibleText)) {
                e.click();
                break;
            }
        }
        // driver.findElement(By.linkText(visibleText)).click();
        sleep(TIME_OUT);
    }

    protected void selectDropDown(By by, String visibleText) throws Exception {
        new Select(driver.findElement(by)).selectByVisibleText(visibleText);
    }

    protected void setFilePath(By by, String file) throws Exception {
        WebElement field = driver.findElement(by);
        field.sendKeys(System.getProperty("user.dir") + "\\src\\test\\resources\\" + file);
    }

    protected void checkPageTitle(String title) throws Exception {
        String pageTitle = driver.getTitle();
        if (pageTitle.equals("")) {
            sleep(2);
        }
        if (!title.equals(pageTitle)) {
            throw new IllegalStateException("This is not the intended page.\nExpected: " + title + "\nActual: " + pageTitle);
        }
    }

    protected String getText(By by) throws Exception {
        return driver.findElement(by).getText();
    }

    protected String getBodyText() throws Exception {
        return getText(By.tagName("BODY"));
    }

    protected void verifyErrors() {
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void waitElementPresent(By by) throws InterruptedException {
        try {
            WebElement webElement = driver.findElement(by);
            if (webElement.isEnabled() && webElement.isDisplayed()) {
                logger.info("Element: " + by + " present, enabled & displayed.");
            } else {
                sleep(1);
                logger.info("Waiting for Element: " + by + " to be enabled & displayed.");
                waitElementPresent(by);
            }
        } catch (NoSuchElementException e) {
            sleep(1);
            logger.info("Waiting for Element: " + by + " to be present.");
            waitElementPresent(by);
        }
    }

    protected boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    protected void validateLabels(String expectedLabels) throws Exception {

        List<String> expectedLabel = new LinkedList<String>(Arrays.asList(expectedLabels.split(",")));
        List<String> actualLabel = new LinkedList<String>(new ArrayList<String>());
        List<String> tempExpectedLabel = new LinkedList<String>(new ArrayList<String>());

        List<WebElement> labelList = driver.findElements(By.tagName("label"));
        for (WebElement webElement : labelList) {
            try {
                actualLabel.add(webElement.getText());
            } catch (StaleElementReferenceException e) {
                labelList = driver.findElements(By.tagName("label"));
                for (WebElement webElement2 : labelList) {
                    actualLabel.add(webElement2.getText());
                }
                break;
            }

        }

        tempExpectedLabel.addAll(expectedLabel);
        expectedLabel.removeAll(actualLabel);
        actualLabel.removeAll(tempExpectedLabel);

        if (expectedLabel.size() > 0 || actualLabel.size() > 0) {
            verificationErrors.append("\nExpected Labels: ");
            verificationErrors.append(expectedLabel.toString());
            verificationErrors.append("\nActual Labels: ");
            verificationErrors.append(actualLabel.toString());
        }
    }

    protected String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    public static JSONObject[][] getCSVToJson(String name) throws Exception {

        String filePath = "src\\test\\resources\\" + name + ".csv";
        BufferedReader br = null;
        String line;
        String[] header;
        JSONObject[][] data = null;
        List<String> rows = new ArrayList<String>();

        try {
            br = new BufferedReader(new FileReader(filePath));

            while ((line = br.readLine()) != null) {
                rows.add(line);
            }

            header = rows.get(0).split(FIELD_DELIMITER);
            data = new JSONObject[rows.size() - 1][1];

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i).split(FIELD_DELIMITER);
                JSONObject record = new JSONObject();

                for (int j = 0; j < header.length; j++) {
                    try {
                        record.put(header[j], row[j]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        record.put(header[j], "");
                    }
                }
                data[i - 1][0] = record;
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            System.exit(0);
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(0);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            System.exit(0);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return data;
    }

    public static JSONObject[][] getExcelToJson(String sheet) throws JSONException, IOException {
        String filePath = "TestData.xls";
        return getExcelToJson(filePath, sheet);
    }

    public static JSONObject[][] getExcelToJson(String file, String _sheet) throws JSONException, IOException {

        String filePath = "src\\test\\resources\\" + file;
        String[] header;

        FileInputStream fileInputStream = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet(_sheet);
        Cell cell = null;
        int columns = sheet.getRow(0).getLastCellNum();
        int rows = sheet.getLastRowNum();
        JSONObject[][] data = new JSONObject[rows - 1][1];
        header = new String[columns];
        Iterator<Cell> headerIterator;
        Iterator<Cell> dataIterator;

        int headerIndex = 0;
        for (headerIterator = sheet.getRow(0).cellIterator(); headerIterator.hasNext(); ) {
            cell = headerIterator.next();
            header[headerIndex++] = cell.getStringCellValue();
        }

        for (int row = 1; row < rows; row++) {
            dataIterator = sheet.getRow(row).cellIterator();
            JSONObject record = new JSONObject();
            for (headerIndex = 0; headerIndex < columns; headerIndex++) {
                try {
                    cell = dataIterator.next();
                    record.put(header[headerIndex], cell.getStringCellValue());

                } catch (IllegalStateException e) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    record.put(header[headerIndex], cell.getStringCellValue());
                } catch (ArrayIndexOutOfBoundsException e) {
                    record.put(header[headerIndex], "");
                }
            }
            data[row - 1][0] = record;
        }
        return data;
    }

    public static JSONObject[][] getTable(By by) throws Exception {

        WebElement[] header;
        JSONObject[][] data;
        WebElement table = driver.findElement(by);
        List<WebElement> rowList;
        List<WebElement> columnList;

        rowList = table.findElements(By.tagName("tr"));
        int rowCount = rowList.size();
        columnList = rowList.get(0).findElements(By.tagName("th"));
        int columnCount = columnList.size();

        data = new JSONObject[rowCount - 1][1];
        header = columnList.toArray(new WebElement[columnList.size()]);

        for (int i = 1; i < rowList.size(); i++) {
            columnList = rowList.get(i).findElements(By.tagName("td"));
            JSONObject record = new JSONObject();
            for (int j = 0; j < header.length; j++) {
                try {
                    record.put(header[j].getText(), columnList.get(j).getText());
                } catch (ArrayIndexOutOfBoundsException e) {
                    record.put(header[j].getText(), "");
                }
            }
            data[i - 1][0] = record;
        }
        return data;
    }

    public void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

}
