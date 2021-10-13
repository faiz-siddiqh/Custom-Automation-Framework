package core_utils;

import static io.restassured.RestAssured.given;

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class APIUtils extends BaseUtils {

	private static String baseURI;
	private static SessionFilter session;
	private static String sessionId;
	private static PrintStream logs;

	/**
	 * Create an API session and authenticate
	 */
	public static void createSession() {
		RestAssured.baseURI = BaseUtils.TestData.getGlobalVariableData("API_URL");
		String username = BaseUtils.TestData.getGlobalVariableData("username");
		String password = BaseUtils.TestData.getGlobalVariableData("password");// find other method or technique to
																				// store the password eg.char array
		session = new SessionFilter();

		given().auth().basic(username, password).filter(session).when().get(baseURI).then().assertThat()
				.statusCode(HttpStatus.SC_OK);
		setSessionId(session.getSessionId());// set session id if you want to hit api with same session

		try {
			logs = new PrintStream(new FileOutputStream(
					System.getProperty("user.dir") + BaseUtils.ProjectProperties.readFromGlobalConfigFile("logs")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return a RequestSpecification Spec with common parameters set
	 * 
	 * @return
	 */
	public static RequestSpecification getRequestSpec() {
		String baseURL = BaseUtils.TestData.getGlobalVariableData("API_URL"); // this is usually same for an application
																				// under test
		// ADD all the required common parameters to this Spec

		return new RequestSpecBuilder().addFilter(session).setBaseUri(baseURL).setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON).addFilter(RequestLoggingFilter.logRequestTo(logs))
				.addFilter(ResponseLoggingFilter.logResponseTo(logs)).build();

	}

	/**
	 * Return a RequestSpecification Spec with common parameters set
	 * 
	 * @return
	 */
	public static ResponseSpecification getResponseSpec() {
		// we can also fetch status code from testdata incase to validate the negative
		// scenarios
		return new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

	}

	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		APIUtils.sessionId = sessionId;
	}

}
