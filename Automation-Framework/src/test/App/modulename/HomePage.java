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

		String email = BaseUtils.TestData.getTestData("Email");
		String password = BaseUtils.TestData.getTestData("password");

		BaseUtils.WebElements.clickAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("homepage-login")),
				"Click on Login Link button");
		BaseUtils.Common.switchToHandle();
		assertTrue(BaseUtils.WebElements.isElementPresent(BaseUtils.Locators.getLocator("homepage-email"),
				"Checking if email field is present"), "Element not present");

		BaseUtils.WebElements.clickAndTypeAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("homepage-email")), email,
				"Click and Type email");
		BaseUtils.WebElements.clickAndTypeAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("homepage-password")), password,
				"Click and Type Password");
		BaseUtils.WebElements.clickAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("login-btn")),
				"Click on Login button");
	}

	@Test
	public void navigateToHomePage_FrontEnd_20002(Method method) {
		homepage.launchAndLogin(method.getName());

		BaseUtils.WebElements.clickAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("homepage-Demo")),
				"Click on Demo button");
		BaseUtils.WebElements.clickAndWait(
				BaseUtils.WebElements.getElementByXpath(BaseUtils.Locators.getLocator("Demo-Homepage-FrontEnd")),
				"Click on HomePage-FrontEnd link-button");
		BaseUtils.Common.switchToHandle();
		assertTrue(BaseUtils.WebElements.isElementPresent(BaseUtils.Locators.getLocator("Demo-FrontEnd-Hotels"),
				"Validating Travel Page is opened"), "Travels Page is not opened-Element not present");

	}

	@AfterMethod
	public void cleanUp(ITestResult testresult) throws Exception {
		if (testresult.getStatus() == ITestResult.FAILURE)
			BaseUtils.Common.cleanUp();
		else if (testresult.getStatus() == ITestResult.SUCCESS)
			BaseUtils.Common.cleanUpOnSuccess(testresult.getName());
		else if (testresult.getStatus() == ITestResult.SKIP)
			BaseUtils.Common.cleanUpOnSkip(testresult.getName());

	}

}
