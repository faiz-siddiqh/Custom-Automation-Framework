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

	public void setUp() throws SAXException, IOException {
		BaseUtils.Common.setClassName("HomePage-Test");
		BaseUtils.Common.setUp("HomePage");
	}

	public void launchAndLogin(String testName) {
		BaseUtils.Common.setMethodName(testName);
		BaseUtils.Common.setExtentTest(testName);		
		BaseUtils.Common.setUpDriver();
		BaseUtils.Common.launchApp();

	}

	

}