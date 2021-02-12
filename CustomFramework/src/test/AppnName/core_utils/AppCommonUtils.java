package core_utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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

public class AppCommonUtils {

	private static Properties properties = new Properties();
	private static WebDriver driver;

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

			String url = ProjectProperties.readFromGlobalConfigFile("URL");
			launch(url);

		}

		public static void launch(String url) {

			driver.manage().window().maximize();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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

			try {
				expr = xpath.compile("//element[@name='" + locatorname + "']/@*");
				NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				Attr attr = (Attr) result.item(0);
				return attr.getNodeValue();
				// return attr.getTextContent();

			} catch (XPathExpressionException e) {
				System.out.println("check the locatorname input value");
				e.printStackTrace();
			}

			return null;
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

	public static void setUp() throws SAXException, IOException {
		setUpDriverAndLocatorsFile();

	}

	public static void setUpDriverAndLocatorsFile() {
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

		/*
		 * Setting up Locators File
		 */

		locators.setUpLocatorsFile("locatorsfile");
	}

}
