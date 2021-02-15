package core_utils;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentTest;

public class HomePage_util {

	public ExtentTest test = AppCommonUtils.common.getExtentTest();

	public void setUp() throws SAXException, IOException {
		AppCommonUtils.common.setClassName("HomePage-Test");
		AppCommonUtils.setUp();

	}

}
