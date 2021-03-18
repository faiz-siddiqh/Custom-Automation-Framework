package modulename;

import core_utils.BaseUtils;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core_utils.HomePage_Util;

public class HomePage {
	public static HomePage_Util homepage = new HomePage_Util();

	@BeforeClass
	public void setUp() throws Exception {
		homepage.setUp();
	}

	@Test
	public void loginwithInvalidcredentials_20001(Method method) {
		homepage.launchAndLogin(method.getName());

		String email = BaseUtils.testData.getTestData("Email");
		String password = BaseUtils.testData.getTestData("password");

		BaseUtils.clickAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("homepage-login")),
				"Click on Login Link button");
		BaseUtils.switchToHandle();
		assertTrue(BaseUtils.isElementPresent(BaseUtils.locators.getLocator("homepage-email"),
				"Checking if email field is present"), "Element not present");

		BaseUtils.clickAndTypeAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("homepage-email")),
				email, "Click and Type email");
		BaseUtils.clickAndTypeAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("homepage-password")),
				password, "Click and Type Password");
		BaseUtils.clickAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("login-btn")),
				"Click on Login button");
	}

	@Test
	public void navigateToHomePage_FrontEnd_20002(Method method) {
		homepage.launchAndLogin(method.getName());

		BaseUtils.clickAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("homepage-Demo")),
				"Click on Demo button");
		BaseUtils.clickAndWait(BaseUtils.getElementByXpath(BaseUtils.locators.getLocator("Demo-Homepage-FrontEnd")),
				"Click on HomePage-FrontEnd link-button");
		BaseUtils.switchToHandle();
		assertTrue(BaseUtils.isElementPresent(BaseUtils.locators.getLocator("Demo-FrontEnd-Hotels"),
				"Validating Travel Page is opened"), "Travels Page is not opened-Element not present");

	}

	@AfterMethod
	public void cleanUp(ITestResult testresult) throws Exception {
		if (testresult.getStatus() == ITestResult.FAILURE)
			BaseUtils.common.cleanUp();
		else if (testresult.getStatus() == ITestResult.SUCCESS)
			BaseUtils.common.cleanUpOnSuccess(testresult.getName());
		else if (testresult.getStatus() == ITestResult.SKIP)
			BaseUtils.common.cleanUpOnSkip(testresult.getName());

	}

}
