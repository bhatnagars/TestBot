package test;

import lib.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import harness.ExtentTestNGReportBuilder;

public class Test1 extends ExtentTestNGReportBuilder {

	private WebActions lib;
	private WebDriver driver;
	private String url;
	private String navigate;

	@Parameters("param")
	@BeforeTest
	public void Test(String param) {
		lib = new WebActions(param);
		driver = lib.getbrowser(param);
		url = lib.getglbdata("URL");
		navigate= lib.getdata("Navigate");
		createTest(param,"Test Description");
	}


@Test
	public void Test() {
		try {
			lib.Launch(url);
			lib.verifytitle("PHPTRAVELS");
			lib.smartsync(5);
			lib.Navigate(navigate);
			lib.smartsync(5);
			lib.entervalue("Login.UserName",lib.getglbdata("username"));
			lib.entervalue("Login.Password",lib.getglbdata("password"));
			lib.jsclick("Login.LoginBtn");
			lib.smartsync(5);
			if(lib.verifyTextPresence("John")) {
				pass("Welcome page is displayed");
			}else {
				fail("welcome page is not displayed");
			}
		} catch (Exception err) {
			System.out.println(err.getMessage());
			fail(err.getMessage());
		} finally {
			driver.quit();
		}
	}
}
