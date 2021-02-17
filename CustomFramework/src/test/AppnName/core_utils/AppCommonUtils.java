package core_utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.util.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AppCommonUtils {

	/**
	 * @author Faiz-Siddiqh
	 * 
	 */
	private static Properties properties = new Properties();
	private static WebDriver driver;
	private static ExtentReports extentreport;
	private static ExtentTest test;
	public static String className;
	

	public static class ProjectProperties {

		public static void loadPropertiesFile(String filePath) {
			try {
				properties.load(new FileReader(System.getProperty("user.dir") + filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public static String readProjectVariables(String propertyName) {

			return properties.getProperty(propertyName);

		}

		public static String readFromGlobalConfigFile(String propertyName) {
			loadPropertiesFile("//CommonFiles//config.properties");
			return properties.getProperty(propertyName);

		}

	}

	public static class common {

		public static void appLogin() {
			try {
				String url = ProjectProperties.readFromGlobalConfigFile("URL");
				launch(url);
			} catch (Exception e) {
				logInfo(e.getMessage());
				cleanUp();
			}

		}

		public static void launch(String url) {
			try {
				if (driver == null) {
					AppCommonUtils.setUpDriver();
				}

				driver.manage().window().maximize();
				logInfo("Maximizing the window");
				driver.get(url);
				logInfo("Navigating to " + url);
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				logInfo("fetching url");

			} catch (Exception e) {
				logInfo(e.getMessage());
				cleanUp();
			}
		}

		public static void setClassName(String className) {
			AppCommonUtils.className = className;
		}

		public static void getExtentReportInstance() {

			String path = System.getProperty("user.dir") + "//TestResults//" + className;
			File resultsFile = new File(path);
			if (resultsFile.exists() && resultsFile.isDirectory()) {
				resultsFile.delete();
			}
			resultsFile.mkdir();
			extentreport = new ExtentReports(path + "//" + "ExtentReport.html", false);
			extentreport.addSystemInfo("Selenium Version", "3.141.59").addSystemInfo("Platform", "Windows");
			// extent.addSystemInfo("Selenium Version",
			// "3.141.59").addSystemInfo("Platform", System.getProperty("os.name"));

		}

		public static void cleanUp(String fileName) throws Exception {
			common.logInfo("This Test Step failed, Capturing Screenshot.");
			AppCommonUtils.Screenshot.takeScreenshot(fileName);
			AppCommonUtils.common.getDriver().quit();
			test.log(LogStatus.FAIL, "Test Failed");
			AppCommonUtils.common.getExtentReport().endTest(test);
			AppCommonUtils.common.getExtentReport().flush();
		}

		public static void cleanUp() {
			common.logInfo("This Test Step failed, Capturing Screenshot.");
			AppCommonUtils.Screenshot.takeScreenshot();
			AppCommonUtils.common.getDriver().quit();
			test.log(LogStatus.FAIL, "Test Failed");
			AppCommonUtils.common.getExtentReport().endTest(test);
			AppCommonUtils.common.getExtentReport().flush();
		}

		public static ExtentReports getExtentReport() {
			return extentreport;
		}

		public static WebDriver getDriver() {
			return driver;
		}

		public static void setExtentTest(String testName) {
			test = extentreport.startTest(testName);
			test.log(LogStatus.INFO, "Setting log report");
			test.log(LogStatus.INFO, "Starting Test-" + testName);

		}

		public static ExtentTest getExtentTest() {
			return test;
		}

		public static void logInfo(String log) {
			test.log(LogStatus.INFO, log);
		}

		public static String getMonth(String monthNo) {

			String monthName = null;

			int month = Integer.parseInt(monthNo);

			switch (month) {

			case 1:
				monthName = "January";
				break;

			case 2:
				monthName = "February";
				break;
			case 3:
				monthName = "March";
				break;
			case 4:
				monthName = "April";
				break;
			case 5:
				monthName = "May";
				break;
			case 6:
				monthName = "June";
				break;
			case 7:
				monthName = "July";
				break;
			case 8:
				monthName = "August";
				break;
			case 9:
				monthName = "September";
				break;
			case 10:
				monthName = "October";
				break;
			case 11:
				monthName = "November";
				break;
			case 12:
				monthName = "December";
				break;

			}
			return monthName;
		}

	}

	public static class locators {
		private static Document doc;
		private static XPath xpath;
		private static XPathExpression expr;

		public static void setUpLocatorsFile(String moduleLocatorFileName) {

			String locatorsFileLocation = ProjectProperties.readProjectVariables(moduleLocatorFileName);
			File file = new File(System.getProperty("user.dir") + locatorsFileLocation);

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = documentFactory.newDocumentBuilder();
				doc = builder.parse(file);
				XPathFactory xpathFactory = XPathFactory.newInstance();
				xpath = xpathFactory.newXPath();
			} catch (ParserConfigurationException e) {
				System.out.println("Xml file parsing failed");
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public static String getLocator(String locatorname) {
			String locator = null;
			try {
				expr = xpath.compile("//element[@name='" + locatorname + "']/@*");
				NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				common.logInfo("Get Locator for " + locatorname);
				Attr attr = (Attr) result.item(0);
				locator = attr.getNodeValue();
				common.logInfo("Get Locator successful- " + locator);
				// return attr.getTextContent();

			} catch (XPathExpressionException e) {
				// System.out.println("check the locatorname input value");
				common.logInfo(e.getMessage());
				AppCommonUtils.common.cleanUp();
				e.printStackTrace();
			}

			return locator;
		}

		/*
		 * This getLocator(String locatorname) method can be used when the xml locator
		 * file is in the format <element name="locatorname" >xpath</element> this
		 * method is for locators1.xml file in LocatorsMapping folder
		 */
//		public static String getLocator(String locatorname) {
		//
//				try {
//					expr = xpath.compile("//element[@name='" + locatorname + "']");
		//
//					NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//					return nodeList.item(0).getTextContent();
		//
//					/*
//					 * Or
//					 * 
//					 */
//					// return expr.evaluate(doc, XPathConstants.STRING).toString();
		//
//				} catch (XPathExpressionException e) {
//					System.out.println("Check the locatorname input value");
//					e.printStackTrace();
//				}
		//
//				return null;
//			}
	}

	public static class Screenshot {

		public static void takeScreenshot(String fileName) throws Exception {

			String path = System.getProperty("user.dir") + "//TestResults//" + className;
			String fullPath = path + "//" + fileName + ".png";
			test.log(LogStatus.INFO, "capturing Screenshot");
			File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(sourceFile, new File(fullPath));
			test.addScreenCapture(fullPath);
			test.log(LogStatus.FAIL, "Test Failed", fullPath);

		}

		public static void takeScreenshot() {

			String path = System.getProperty("user.dir") + "//TestResults//" + className;
			String fullPath = path + "//" + getRandomString(5) + ".png";
			File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			test.log(LogStatus.INFO, "capturing Screenshot");
			try {
				FileUtils.copyFile(sourceFile, new File(fullPath));
			} catch (Exception e) {
				test.log(LogStatus.WARNING, e.getMessage());
			}
			test.addScreenCapture(fullPath);
			test.log(LogStatus.FAIL, "Test Failed", fullPath);

		}

		public static String getRandomString(int length) {
			StringBuilder sb = new StringBuilder();
			String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

			for (int i = 0; i < str.length(); i++) {
				int index = (int) (Math.random() * str.length());
				sb.append(str.charAt(index));
			}

			return sb.toString();
		}
	}

	public static void setUp() throws SAXException, IOException {

		// setting up an extent report
		common.getExtentReportInstance();
		setUpDriver();
		/*
		 * Setting up Locators File
		 */
		locators.setUpLocatorsFile("locatorsfile");

	}

	public static void setUpDriver() {
		// Load the properties file using this method which contains baseURL and
		// WebDriverType
		String driverLocation;

		String driverName = ProjectProperties.readFromGlobalConfigFile("driver");
		// String baseURL = projectDetails.getProperty("baseURL");

		if (driverName.equalsIgnoreCase("ChromeDriver")) {
			// Set System Property to instantiate ChromeDriver with the path of
			// chromedriver.

			driverLocation = ProjectProperties.readFromGlobalConfigFile("chromedriver");
			if (System.getProperty("os.name").equalsIgnoreCase("Windows")) {// the path varies for windows and Mac
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + driverLocation);
			} else {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + driverLocation.replaceAll(".exe", ""));
			}
			// Set Options using for chrome using the below commented line

			// ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver();

		} else if (driverName.equalsIgnoreCase("FireFox")) {
			// Set System Property to instantiate ChromeDriver with the path of
			// firefoxdriver.
			driverLocation = ProjectProperties.readFromGlobalConfigFile("firefoxdriver");

			if (System.getProperty("os.name").equalsIgnoreCase("Windows")) {// the path varies for windows and Mac
				System.setProperty("webdriver.firefox.driver", System.getProperty("user.dir") + driverLocation);
			} else {
				System.setProperty("webdriver.firefox.driver",
						System.getProperty("user.dir") + driverLocation.replaceAll(".exe", ""));
			}

			// Set Options using for Firefox
			FirefoxProfile profile = new FirefoxProfile();
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);

			driver = new FirefoxDriver();
		}

//			driver.manage().window().maximize();      //--Implemented in login()method
//			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

	}

	public static WebElement getElement(String locator, String type) {
		WebElement element = null;
		type = type.toLowerCase();
		try {
			if (type.equals("id")) {
				element = driver.findElement(By.id(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("xpath")) {
				element = driver.findElement(By.xpath(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("cssselector")) {
				element = driver.findElement(By.cssSelector(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("name")) {
				element = driver.findElement(By.name(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("classname")) {
				element = driver.findElement(By.className(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("tagname")) {
				element = driver.findElement(By.tagName(locator));
				common.logInfo("Element found -" + element);
			} else if (type.equals("linktext")) {
				element = driver.findElement(By.linkText(locator));
				common.logInfo("Element found -" + element);
			}
		} catch (Exception e) {
			common.logInfo("Locator not supported or check type");
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		return element;
	}

	public static WebElement getElementByXpath(String locator) {
		WebElement element = null;
		try {
			common.logInfo("Lookup for Element-" + locator);
			element = driver.findElement(By.xpath(locator));
			common.logInfo("Element found -" + element);

		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		return element;
	}

	public static boolean isElementPresent(String locator) {
		try {
			if (getElementByXpath(locator).isDisplayed()) {
				common.logInfo("Element is Displayed-" + locator);
				return true;
			}
		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		return false;
	}

	public static boolean isElementEnabled(String locator) {
		try {
			if (getElementByXpath(locator).isEnabled()) {
				common.logInfo("Element is Enabled-" + locator);
				return true;
			}
		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		return false;
	}

	public static List<WebElement> getElementsByTagname(WebElement element, String tagname) {
		try {
			return element.findElements(By.tagName(tagname));
		} catch (Exception e) {
			common.logInfo("Locator not found -check element and type");
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		return null;

	}

	public static List<WebElement> getElements(String locator, String type) {

		type = type.toLowerCase();
		try {
			if (type.equals("id")) {
				return driver.findElements(By.id(locator));
			} else if (type.equals("xpath")) {
				return driver.findElements(By.xpath(locator));
			} else if (type.equals("cssselector")) {
				return driver.findElements(By.cssSelector(locator));
			} else if (type.equals("name")) {
				return driver.findElements(By.name(locator));
			} else if (type.equals("classname")) {
				return driver.findElements(By.className(locator));
			} else if (type.equals("tagname")) {
				return driver.findElements(By.tagName(locator));
			} else if (type.equals("linktext")) {
				return driver.findElements(By.linkText(locator));
			}
		} catch (Exception e) {
			common.logInfo("Locator not supported or check type");
			common.logInfo(e.getMessage());
			common.cleanUp();
		}
		return null;
	}

	public static void findElementAndClick(List<WebElement> list, String requiredText) {
		try {
			for (WebElement eachElement : list) {
				if (eachElement.getText().contains(requiredText)) {
					clickAndWait(eachElement);
					common.logInfo("clicked on " + requiredText);
					break;
				}
			}
		} catch (Exception e) {
			common.logInfo(e.getMessage());
//			driver.quit();
//			extentreport.endTest(test);
//			extentreport.flush();
			common.cleanUp();
		}

	}

	public static void clickAndWait(WebElement element) {
		try {
			element.click();
			common.logInfo("click on element" + element);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		} catch (Exception e) {
			common.logInfo(e.getMessage());
//			driver.quit();
//			extentreport.endTest(test);
//			extentreport.flush();
			common.cleanUp();
		}

	}

	public static void clickAndTypeAndWait(WebElement element, String keysToSend) {
		try {
			clickAndWait(element);
			element.sendKeys(keysToSend);
			common.logInfo("Entered text -" + keysToSend);
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}
	}

	public static void dragAndDrop(WebElement fromElement, WebElement toElement) {
		try {
			Actions action = new Actions(driver);
			// 1)
			action.dragAndDrop(fromElement, toElement).build().perform();
		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

		/*
		 * 2)
		 * action.clickAndHold(fromElement).moveToElement(toElement).build().perform();
		 */
	}

	public static void slider(WebElement sliderElement, int xOffset, int yOffset) {
		try {
			Actions action = new Actions(driver);

			action.dragAndDropBy(sliderElement, xOffset, yOffset).perform();

		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}
	}

	public static void selectFromDropdown(WebElement element, String textToBeSelected) {
		try {
			Select select = new Select(element);
			select.selectByVisibleText(textToBeSelected);
			common.logInfo(textToBeSelected + " selected");

		} catch (Exception e) {
			common.logInfo(e.getMessage());
			common.cleanUp();
		}

	}

}
