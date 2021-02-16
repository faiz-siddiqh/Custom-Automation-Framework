package core_utils;

import java.io.IOException;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author Faiz-Siddiqh
 *
 */
public class HomePage_util {

	public ExtentTest test = AppCommonUtils.common.getExtentTest();

	public void setUp() throws SAXException, IOException {
		AppCommonUtils.common.setClassName("HomePage-Test");
		AppCommonUtils.setUp();

	}

	public void launchAndLogin(String testName) {

		AppCommonUtils.common.setExtentTest(testName);
		AppCommonUtils.setUpDriver();
		AppCommonUtils.common.appLogin();

	}

	public void afterSuccessfullTest(String testName) {
		AppCommonUtils.common.getDriver().quit();
		test.log(LogStatus.PASS, "Test Passed-" + testName);
		AppCommonUtils.common.getExtentReport().flush();
	}

}
