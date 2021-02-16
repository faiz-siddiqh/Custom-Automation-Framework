package appmodulename.Test;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core_utils.AppCommonUtils;
import core_utils.HomePage_util;

public class HomePageTest {
	public HomePage_util homepage;

	@BeforeClass
	public void setUp() throws Exception {
		homepage.setUp();
	}

	@BeforeMethod
	public void launch(ITestContext context) {
		homepage.launchAndLogin(context.getName());

	}

	@Test
	public void testLoginWithInvalidCredentials() {

	}

	@AfterClass
	public void closeAll() {

		AppCommonUtils.common.getExtentReport().flush();
	}

	@AfterMethod
	public void cleanUp(ITestResult testresult) throws Exception {
		if (testresult.getStatus() == ITestResult.FAILURE) {
			AppCommonUtils.common.cleanUp(testresult.getName());
		}
		homepage.afterSuccessfullTest(testresult.getTestName());

	}

}
