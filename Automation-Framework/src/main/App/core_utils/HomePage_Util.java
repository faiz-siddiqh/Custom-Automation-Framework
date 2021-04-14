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
public class HomePage_Util {

	public ExtentTest test = BaseUtils.common.getExtentTest();

	public void setUp() throws SAXException, IOException {
		BaseUtils.common.setClassName("HomePage-Test");
		BaseUtils.setUp("HomePage");
	}

	public void launchAndLogin(String testName) {
		BaseUtils.common.setMethodName(testName);
		BaseUtils.common.setExtentTest(testName);		
		BaseUtils.setUpDriver();
		BaseUtils.common.appLogin();

	}

	

}